package ru.itis.bookmatch.data

import ru.itis.bookmatch.domain.Book

interface BookRepository {

    suspend fun getBookForSwipe(): List<Book>

}