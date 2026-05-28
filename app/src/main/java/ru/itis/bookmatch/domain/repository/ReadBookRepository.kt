package ru.itis.bookmatch.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.itis.bookmatch.domain.ReadBook

interface ReadBookRepository {

    fun getReadBooksFlow(userId: String): Flow<List<ReadBook>>

    suspend fun addToRead(userId: String, readBook: ReadBook)

    suspend fun isRead(userId: String, bookId: String): Boolean

    suspend fun deleteFromRead(userId: String, bookId: String)

    suspend fun updateFeedback(userId: String, bookId: String, feedback: String)

    suspend fun updateRating(userId: String, bookId: String, rating: Double)

    suspend fun syncWithFirestore(userId: String)

    suspend fun getBooksCount(userId: String): Int

    suspend fun getLastReadBook(userId: String): Flow<List<ReadBook>>

    suspend fun getById(userId: String, bookId: String): ReadBook?
}