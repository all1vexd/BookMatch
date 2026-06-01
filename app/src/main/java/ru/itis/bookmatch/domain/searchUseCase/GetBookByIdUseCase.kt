package ru.itis.bookmatch.domain.searchUseCase

import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.repository.BookRepository
import javax.inject.Inject

class GetBookByIdUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(bookId: String): Book {
        return bookRepository.getBookById(bookId = bookId)
    }
}