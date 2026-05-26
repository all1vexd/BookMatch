package ru.itis.bookmatch.presentation.screens.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.GetBooksForSwipeUseCase
import ru.itis.bookmatch.domain.likedUseCase.AddToLikedUseCase

class MainScreenViewModel @AssistedInject constructor(
    @Assisted("userId") private val userId: String,
    private val getBooksForSwipeUseCase: GetBooksForSwipeUseCase,
    private val addToLikedUseCase: AddToLikedUseCase
): ViewModel() {

    private var prefetchJob: Job? = null
    private var prefetchBooks: List<Book> = emptyList()
    private val seenBookIds = mutableSetOf<String>()
    private var batchIndex = 0

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userId") userId: String
        ): MainScreenViewModel
    }

    private val _state = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        prefetchJob?.cancel()
        prefetchJob = null
        prefetchBooks = emptyList()
        viewModelScope.launch {
            _state.value = MainScreenState.Loading
            try {
                fetchAndApplyBooks()
            } catch (e: Exception) {
                var success = false
                for (i in 1..3) {
                    try {
                        fetchAndApplyBooks()
                        success = true
                        break
                    } catch (e: Exception) {
                        delay(1000L)
                    }
                }
                if (!success) {
                    _state.value = MainScreenState.Error
                }
            }
        }
    }

    private suspend fun fetchAndApplyBooks() {
        val books = getBooksForSwipeUseCase(userId, batchIndex)
        val freshBooks = books.filter { it.id !in seenBookIds }
        seenBookIds.addAll(freshBooks.map { it.id })
        batchIndex += 5
        if (freshBooks.isEmpty()) throw Exception("No book loaded")
        _state.value = MainScreenState.Content(bookList = freshBooks, currentIndex = 0)
    }

    fun processCommand(command: MainScreenCommand) {
        when (command) {

            MainScreenCommand.RightSwipe -> {
                val currentState = _state.value as? MainScreenState.Content ?: return
                val currentBook = currentState.bookList.getOrNull(currentState.currentIndex)
                goToNextBook()
                if (currentBook != null) {
                    viewModelScope.launch {
                        addToLikedUseCase(userId, currentBook)
                    }
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

    private fun goToNextBook() {
        val currentState = _state.value as? MainScreenState.Content ?: return
        val remaining = currentState.bookList.size - currentState.currentIndex

        if (remaining == 5 && prefetchJob == null) {
            prefetchJob = viewModelScope.launch {
                runCatching { getBooksForSwipeUseCase(userId, batchIndex) }
                    .onSuccess {
                        val freshBooks = it.filter { book ->
                            book.id !in seenBookIds
                        }
                        seenBookIds.addAll(freshBooks.map { it.id })
                        prefetchBooks = freshBooks
                        batchIndex += 5
                    }
                prefetchJob = null
            }
        }

        if (currentState.currentIndex + 1 < currentState.bookList.size) {
            _state.update { state ->
                if (state is MainScreenState.Content) {
                    state.copy(currentIndex = currentState.currentIndex + 1)
                } else {
                    state
                }
            }
        } else {
            if (prefetchBooks.isNotEmpty()) {
                _state.value = MainScreenState.Content(bookList = prefetchBooks)
                prefetchBooks = emptyList()
                prefetchJob = null
            } else {
                loadData()
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
        val currentIndex: Int = 0
    ) : MainScreenState
}