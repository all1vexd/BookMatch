package ru.itis.bookmatch.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.itis.bookmatch.data.BookRepository

class GetBooksForSwipeUseCase(
    private val repository: BookRepository
) {

    suspend operator fun invoke(): List<Book> {
        return repository.getBookForSwipe()
    }

}