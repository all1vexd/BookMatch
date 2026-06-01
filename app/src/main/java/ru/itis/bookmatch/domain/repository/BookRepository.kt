package ru.itis.bookmatch.domain.repository

import ru.itis.bookmatch.domain.Book

interface BookRepository {

    suspend fun getBookForSwipe(query: String, startIndex: Int, maxResults: Int = 5): List<Book>

    suspend fun searchBooks(query: String, startIndex: Int, maxResults: Int = 20): List<Book>

    suspend fun getBookById(bookId: String): Book
}