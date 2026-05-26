package ru.itis.bookmatch.domain

interface BookRepository {

    suspend fun getBookForSwipe(query: String, startIndex: Int, maxResults: Int = 5): List<Book>

}
