package ru.itis.bookmatch.data.repository

import ru.itis.bookmatch.data.network.GoogleBooksApi
import ru.itis.bookmatch.data.toBook
import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.repository.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val api: GoogleBooksApi
) : BookRepository {

    override suspend fun getBookForSwipe(query: String, startIndex: Int, maxResults: Int): List<Book> {
        return api.getBooks(query = query, maxResults = maxResults, startIndex = startIndex)
            .items
            .orEmpty()
            .map { 
                it.toBook()
            }
    }

    override suspend fun searchBooks(query: String, startIndex: Int, maxResults: Int): List<Book> {
        return api.getBooks(query = "intitle:${query}", maxResults = maxResults, startIndex = startIndex)
            .items
            .orEmpty()
            .map {
                it.toBook()
            }
    }

    override suspend fun getBookById(bookId: String): Book {
        return api.getBookById(id = bookId).toBook()
    }
}