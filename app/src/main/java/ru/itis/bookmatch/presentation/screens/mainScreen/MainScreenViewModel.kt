package ru.itis.bookmatch.presentation.screens.mainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.GetBooksForSwipeUseCase

class MainScreenViewModel(
    private val getBooksForSwipeUseCase: GetBooksForSwipeUseCase
): ViewModel() {

    private val _state = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _state.value = MainScreenState.Loading
            try {
                val books = getBooksForSwipeUseCase()
                _state.value = MainScreenState.Content(
                    bookList = books,
                    currentIndex = 0
                )
            } catch (e: Exception) {
                Log.e("TAPI_TES", "Error: ${e.message}")
                _state.value = MainScreenState.Error
            }
        }
    }

    fun processCommand(command: MainScreenCommand) {
        when (command) {

            MainScreenCommand.RightSwipe -> {
                val currentState = _state.value
                if (currentState is MainScreenState.Content) {
                    val currentBook = currentState.bookList.getOrNull(currentState.currentIndex)
                    if (currentBook != null) {
                        _state.update { state ->
                            if (state is MainScreenState.Content) {
                                state.copy(
                                    likedBooks = state.likedBooks + currentBook
                                )
                            } else {
                                state
                            }
                        }
                    }
                    goToNextBook()
                }
            }

            MainScreenCommand.LeftSwipe -> {
                val currentState = _state.value
                if (currentState is MainScreenState.Content) {
                    goToNextBook()
                }
            }

        }
    }

    fun goToNextBook() {

        val currentState = _state.value

        if (currentState is MainScreenState.Content) {
            val currentIndexFromState = currentState.currentIndex
            val totalBooks = currentState.bookList.size

            if (currentIndexFromState + 1 < totalBooks) {
                _state.update { state ->
                    if (state is MainScreenState.Content) {
                        state.copy(
                            currentIndex = currentIndexFromState + 1
                        )
                    } else {
                        state
                    }
                }
            }
        }
    }
}

sealed interface MainScreenCommand {

    data object RightSwipe: MainScreenCommand

    data object LeftSwipe: MainScreenCommand

}

sealed interface MainScreenState {

    data object Loading: MainScreenState

    data object  Error: MainScreenState

    data class Content(
        val bookList: List<Book> = listOf(),
        val likedBooks: List<Book> = listOf(),
        val currentIndex: Int = 0
    ) : MainScreenState
}