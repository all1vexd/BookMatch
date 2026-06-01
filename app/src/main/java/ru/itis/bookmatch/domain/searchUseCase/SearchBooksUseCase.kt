package ru.itis.bookmatch.domain.searchUseCase

import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.repository.BookRepository
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(query: String, startIndex: Int = 0): List<Book> {
        return bookRepository.searchBooks(query = query, startIndex = startIndex)
    }
}