package ru.itis.bookmatch.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.searchUseCase.SearchBooksUseCase
import javax.inject.Inject

class SearchScreenViewModel @AssistedInject constructor(
    @Assisted("userId") private val userId: String,
    private val searchBooksUseCase: SearchBooksUseCase
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userId") userId: String
        ): SearchScreenViewModel
    }

    private val _state = MutableStateFlow<SearchScreenState>(SearchScreenState.Idle)
    val state = _state.asStateFlow()

    private var currentQuery = ""
    private var currentStartIndex = 0
    private var isLoadingMore = false

    fun processCommand(command: SearchScreenCommand) {
        when (command) {
            is SearchScreenCommand.Search -> {
                currentQuery = command.query
                currentStartIndex = 0
                search(reset = true)
            }

            SearchScreenCommand.LoadMore -> {
                if (!isLoadingMore) search(reset = false)
            }
        }
    }

    private fun search(reset: Boolean) {
        if (currentQuery.isBlank()) {
            _state.value = SearchScreenState.Idle
            return
        }
        viewModelScope.launch {
            if (reset) {
                _state.value = SearchScreenState.Loading
            } else {
                isLoadingMore = true
            }
            val current = (_state.value as? SearchScreenState.Content)?.searchedBooks ?: emptyList()
            runCatching {
                searchBooksUseCase(currentQuery, currentStartIndex)
            }.onSuccess { books ->
                _state.value = SearchScreenState.Content(
                    searchedBooks = if (reset) books else current + books,
                    canLoadMore = books.size == 20
                )
                currentStartIndex += books.size
            }.onFailure { e ->
                _state.value = SearchScreenState.Error(errorMessage = e.message ?: "")
            }
            isLoadingMore = false
        }
    }

}

sealed interface SearchScreenCommand {

    data class Search(
        val query: String
    ): SearchScreenCommand

    data object LoadMore: SearchScreenCommand

}

sealed interface SearchScreenState {

    data class Content(
        val searchedBooks: List<Book> = emptyList(),
        val canLoadMore: Boolean
    ) : SearchScreenState

    data class Error(
        val errorMessage: String
    ) : SearchScreenState

    data object Loading : SearchScreenState

    data object Idle: SearchScreenState
}