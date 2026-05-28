package ru.itis.bookmatch.domain.repository

import ru.itis.bookmatch.domain.Book

interface BookRepository {

    suspend fun getBookForSwipe(query: String, startIndex: Int, maxResults: Int = 5): List<Book>

}