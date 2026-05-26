package ru.itis.bookmatch.data.repository

import ru.itis.bookmatch.data.network.GoogleBooksApi
import ru.itis.bookmatch.data.toBook
import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val api: GoogleBooksApi
) : BookRepository {

    override suspend fun getBookForSwipe(query: String, maxResults: Int, startIndex: Int): List<Book> {
        return api.getBookForSwipe(query = query, maxResults = maxResults, startIndex = startIndex).items.orEmpty().map { it.toBook() }
    }

}