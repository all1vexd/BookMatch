package ru.itis.bookmatch.domain.repository

import ru.itis.bookmatch.domain.Book

interface CachedBookRepository {

    suspend fun insert(userId: String, book: Book)

    suspend fun getCachedBookById(userId: String, bookId: String): Book?

    suspend fun deleteAll(userId: String)
}