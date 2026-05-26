package ru.itis.bookmatch.presentation.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bookmatch.domain.ReadBook
import ru.itis.bookmatch.domain.readUseCase.GetReadBooksUseCase
import ru.itis.bookmatch.domain.readUseCase.UpdateFeedbackUseCase
import ru.itis.bookmatch.domain.readUseCase.UpdateRatingUseCase

class LibraryScreenViewModel @AssistedInject constructor(
    @Assisted("userId") private val userId: String,
    private val getReadBooksUseCase: GetReadBooksUseCase,
    private val updateFeedbackUseCase: UpdateFeedbackUseCase,
    private val updateRatingUseCase: UpdateRatingUseCase
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userId") userId: String
        ): LibraryScreenViewModel
    }

    private val _state = MutableStateFlow<LibraryScreenState>(LibraryScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _state.value = LibraryScreenState.Loading
            try {
                getReadBooksUseCase(userId).collect { books ->
                    _state.value = LibraryScreenState.Content(
                        readBooks = books
                    )
                }
            } catch (e: Exception) {
                _state.value = LibraryScreenState.Error(
                    errorMessage = e.message ?: ""
                )
            }
        }
    }

    fun processCommand(command: LibraryScreenCommand) {
        when (command) {
            is LibraryScreenCommand.OpenRatingDialog -> {
                _state.update {
                    (it as LibraryScreenState.Content).copy(dialogBookId = command.bookId)
                }
            }
            LibraryScreenCommand.CloseDialog -> {
                _state.update {
                    (it as LibraryScreenState.Content).copy(dialogBookId = null)
                }
            }
            is LibraryScreenCommand.SaveReview -> {
                viewModelScope.launch {
                    try {
                        updateRatingUseCase(userId = userId, bookId = command.bookId, rating = command.rating)
                        updateFeedbackUseCase(userId = userId, bookId = command.bookId, feedback = command.feedback)
                        _state.update {
                            (it as LibraryScreenState.Content).copy(dialogBookId = null)
                        }
                    } catch (e: Exception) {
                        _state.value = LibraryScreenState.Error(errorMessage = e.message ?: "")
                    }
                }

            }
        }
    }

}

sealed interface LibraryScreenCommand {

    data class OpenRatingDialog(val bookId: String): LibraryScreenCommand

    data class SaveReview(
        val bookId: String,
        val rating: Double,
        val feedback: String
    ): LibraryScreenCommand

    data object CloseDialog: LibraryScreenCommand
}

sealed interface LibraryScreenState {

    data object Loading: LibraryScreenState

    data class Error(val errorMessage: String): LibraryScreenState

    data class Content(
        val readBooks: List<ReadBook>,
        val dialogBookId: String? = null
    ): LibraryScreenState
}