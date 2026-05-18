package ru.itis.bookmatch.presentation.screens.saved

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.likedUseCase.GetLikedBooksUseCase
import ru.itis.bookmatch.domain.likedUseCase.RemoveFromLikedBooksUseCase
import ru.itis.bookmatch.presentation.screens.mainScreen.MainScreenState
import javax.inject.Inject

class SavedScreenViewModel @AssistedInject constructor(
    @Assisted("userId") private val userId: String,
    private val removeFromLikedBooksUseCase: RemoveFromLikedBooksUseCase,
    private val getLikedBooksUseCase: GetLikedBooksUseCase
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userId") userId: String
        ): SavedScreenViewModel
    }
    private val _state = MutableStateFlow<SavedScreenState>(SavedScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _state.value = SavedScreenState.Loading
            try {
                getLikedBooksUseCase(userId).collect { books ->
                    _state.value = SavedScreenState.Content(
                        likedBooks = books
                    )
                }
            } catch (e: Exception) {
                _state.value = SavedScreenState.Error(
                    errorMessage = e.message ?: ""
                )
            }
        }
    }

    fun processCommand(command: SavedScreenCommand) {
        when (command) {
            SavedScreenCommand.BookClick -> {
                TODO("Открыть экран подробной информации")
            }
            is SavedScreenCommand.RemoveBook -> {
                viewModelScope.launch {
                    removeFromLikedBooksUseCase(userId, command.bookId)
                }
            }
        }
    }

}

sealed interface SavedScreenCommand {

    data class RemoveBook(val bookId: String): SavedScreenCommand

    data object BookClick: SavedScreenCommand
}

sealed interface SavedScreenState {

    data object Loading: SavedScreenState

    data class Content(
        val likedBooks: List<Book>
    ) : SavedScreenState

    data class Error(
        val errorMessage: String
    ) : SavedScreenState
}