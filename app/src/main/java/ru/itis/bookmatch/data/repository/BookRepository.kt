package ru.itis.bookmatch.data.repository

import ru.itis.bookmatch.domain.Book

interface BookRepository {

    suspend fun getBookForSwipe(): List<Book>

}