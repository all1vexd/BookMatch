package ru.itis.bookmatch.presentation.screens.saved

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.presentation.screens.mainScreen.MainScreenState

class SavedScreenViewModel(

): ViewModel() {

    private val _state = MutableStateFlow<SavedScreenState>(SavedScreenState.Loading)
    val state = _state.asStateFlow()

    fun processCommand(command: SavedScreenCommand) {
        when (command) {
            SavedScreenCommand.BookClick -> {

            }
            SavedScreenCommand.RemoveBook -> {

            }
        }
    }

}

sealed interface SavedScreenCommand {

    data object RemoveBook: SavedScreenCommand

    data object BookClick: SavedScreenCommand
}

sealed interface SavedScreenState {

    data object Loading: SavedScreenState

    data class Content(
        val likedBooks: List<Book>
    )

    data class Error(
        val errorMessage: String
    )
}