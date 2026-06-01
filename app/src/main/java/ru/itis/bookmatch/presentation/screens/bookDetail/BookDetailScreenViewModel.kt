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
import ru.itis.bookmatch.domain.cachedUseCase.GetCachedBookUseCase
import ru.itis.bookmatch.domain.likedUseCase.AddToLikedUseCase
import ru.itis.bookmatch.domain.likedUseCase.GetLikedBookUseCase
import ru.itis.bookmatch.domain.likedUseCase.RemoveFromLikedBooksUseCase
import ru.itis.bookmatch.domain.readUseCase.AddToReadUseCase
import ru.itis.bookmatch.domain.readUseCase.GetReadBookUseCase
import ru.itis.bookmatch.domain.searchUseCase.GetBookByIdUseCase

class BookDetailScreenViewModel @AssistedInject constructor(
    @Assisted("userId") private val userId: String,
    @Assisted("bookId") private val bookId: String,
    private val addToLikedUseCase: AddToLikedUseCase,
    private val addToReadUseCase: AddToReadUseCase,
    private val removeFromLikedBooksUseCase: RemoveFromLikedBooksUseCase,
    private val getLikedBookUseCase: GetLikedBookUseCase,
    private val getReadBookUseCase: GetReadBookUseCase,
    private val getCachedBookUseCase: GetCachedBookUseCase,
    private val getBookByIdUseCase: GetBookByIdUseCase
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userId") userId: String,
            @Assisted("bookId") bookId: String
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
                val likedBook = getLikedBookUseCase(userId = userId, bookId = bookId)
                val readBook = getReadBookUseCase(userId = userId, bookId = bookId)
                val cachedBook = getCachedBookUseCase(userId = userId, bookId = bookId)
                val book = likedBook ?: readBook?.book ?: cachedBook ?: getBookByIdUseCase(bookId = bookId)
                if (book == null) {
                    _state.value = BookDetailScreenState.Error("Книга не найдена")
                } else {
                    _state.value = BookDetailScreenState.Content(
                        isLiked = likedBook != null,
                        isRead = readBook != null,
                        book = book
                    )
                }


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
                        val book = getLikedBookUseCase(userId = userId, bookId = bookId)
                        val cachedBook = getCachedBookUseCase(userId = userId, bookId = bookId)
                        if (book != null) {
                            addToReadUseCase(userId = userId, readBook = ReadBook(book = book))
                            removeFromLikedBooksUseCase(userId = userId, bookId = book.id)
                            _state.update {
                                (it as BookDetailScreenState.Content).copy(
                                    isRead = true,
                                    isLiked = false
                                )
                            }
                        } else if (cachedBook != null) {
                            addToReadUseCase(
                                userId = userId,
                                readBook = ReadBook(book = cachedBook)
                            )
                            _state.update {
                                (it as BookDetailScreenState.Content).copy(isRead = true)
                            }
                        } else {
                            val searchedBook = (_state.value as BookDetailScreenState.Content).book
                            addToReadUseCase(
                                userId = userId,
                                readBook = ReadBook(book = searchedBook)
                            )
                            _state.update {
                                (it as BookDetailScreenState.Content).copy(isRead = true)
                            }
                        }
                    } catch (e: Exception) {
                        _state.value = BookDetailScreenState.Error(errorMessage = e.message ?: "")
                    }
                }

            }
            BookDetailScreenCommand.ToggleLiked -> {
                viewModelScope.launch {
                    try {
                        val currentState = _state.value as? BookDetailScreenState.Content ?: return@launch
                        if (currentState.isLiked) {
                            removeFromLikedBooksUseCase(userId = userId, bookId = bookId)
                            _state.update { (it as BookDetailScreenState.Content).copy(isLiked = false) }
                        } else {
                            addToLikedUseCase(userId = userId, book = currentState.book)
                            _state.update { (it as BookDetailScreenState.Content).copy(isLiked = true) }
                        }
                    } catch (e: Exception) {
                        _state.value = BookDetailScreenState.Error(errorMessage = e.message ?: "")
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