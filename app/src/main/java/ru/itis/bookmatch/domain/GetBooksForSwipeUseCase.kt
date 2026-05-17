package ru.itis.bookmatch.domain

import ru.itis.bookmatch.data.repository.BookRepository

class GetBooksForSwipeUseCase(
    private val repository: BookRepository
) {

    suspend operator fun invoke(): List<Book> {
        return repository.getBookForSwipe()
    }

}