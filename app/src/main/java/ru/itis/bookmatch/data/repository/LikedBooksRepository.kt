package ru.itis.bookmatch.data.repository

import kotlinx.coroutines.flow.Flow
import ru.itis.bookmatch.domain.Book

interface LikedBooksRepository {

    fun getLikedBooksFlow(userId: String): Flow<List<Book>>

    suspend fun getLikedBook(userId: String, bookId: String): Book

    suspend fun addToLiked(userId: String, book: Book)

    suspend fun isLiked(userId: String, bookId: String): Boolean

    suspend fun deleteFromLiked(userId: String, bookId: String)

    suspend fun syncWithFirestore(userId: String)
}