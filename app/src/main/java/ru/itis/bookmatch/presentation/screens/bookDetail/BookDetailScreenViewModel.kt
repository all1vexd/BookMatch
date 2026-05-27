package ru.itis.bookmatch.presentation.screens.bookDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.ReadBook
import ru.itis.bookmatch.domain.likedUseCase.AddToLikedUseCase
import ru.itis.bookmatch.domain.likedUseCase.GetLikedBooksUseCase
import ru.itis.bookmatch.domain.likedUseCase.IsLikedUseCase
import ru.itis.bookmatch.domain.likedUseCase.RemoveFromLikedBooksUseCase
import ru.itis.bookmatch.domain.readUseCase.AddToReadUseCase
import ru.itis.bookmatch.domain.readUseCase.GetReadBooksUseCase
import ru.itis.bookmatch.domain.readUseCase.IsReadUseCase

class BookDetailScreenViewModel @AssistedInject constructor(
    @Assisted("userId") private val userId: String,
    @Assisted("book") private val book: Book,
    private val isLikedUseCase: IsLikedUseCase,
    private val isReadUseCase: IsReadUseCase,
    private val addToLikedUseCase: AddToLikedUseCase,
    private val addToReadUseCase: AddToReadUseCase,
    private val removeFromLikedBooksUseCase: RemoveFromLikedBooksUseCase
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userId") userId: String,
            @Assisted("book") book: Book
        ) : BookDetailScreenViewModel
    }

    private val _state = MutableStateFlow<BookDetailScreenState>(BookDetailScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.value = BookDetailScreenState.Loading
            try {
                val isRead = isReadUseCase(userId = userId, bookId = book.id)
                val isLiked = isLikedUseCase(userId = userId, bookId = book.id)
                _state.value = BookDetailScreenState.Content(
                    isLiked = isLiked,
                    isRead = isRead,
                    book = book
                )
            } catch (e: Exception) {
                _state.value = BookDetailScreenState.Error(errorMessage = e.message ?: "")
            }
        }
    }

    fun processCommand(command: BookDetailScreenCommand) {
        when (command) {
            BookDetailScreenCommand.MarkAsRead -> {
                viewModelScope.launch {
                    try {
                        addToReadUseCase(userId = userId, readBook = ReadBook(book = book))
                        removeFromLikedBooksUseCase(userId = userId, bookId = book.id)
                        _state.value = BookDetailScreenState.Content(
                            isRead = true,
                            isLiked = false,
                            book = book
                        )
                    } catch (e: Exception) {
                        _state.value = BookDetailScreenState.Error(errorMessage = e.message ?: "")
                    }
                }

            }
            BookDetailScreenCommand.ToggleLiked -> {
                val currentState = _state.value as? BookDetailScreenState.Content ?: return
                viewModelScope.launch {
                    val isBookLiked = currentState.isLiked
                    if (isBookLiked) {
                        removeFromLikedBooksUseCase(userId, book.id)
                        _state.update {
                            (it as BookDetailScreenState.Content).copy(isLiked = !isBookLiked)
                        }
                    } else {
                        addToLikedUseCase(userId, book)
                        _state.update {
                            (it as BookDetailScreenState.Content).copy(isLiked = !isBookLiked)
                        }
                    }
                }
            }
        }
    }
}

sealed interface BookDetailScreenCommand {

    data object ToggleLiked: BookDetailScreenCommand

    data object MarkAsRead: BookDetailScreenCommand
}

sealed interface BookDetailScreenState {

    data object Loading: BookDetailScreenState

    data class Error(val errorMessage: String): BookDetailScreenState

    data class Content(val isRead: Boolean, val isLiked: Boolean, val book: Book): BookDetailScreenState
}