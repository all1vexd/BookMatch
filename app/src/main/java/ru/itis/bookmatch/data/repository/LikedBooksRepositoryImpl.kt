package ru.itis.bookmatch.data.repository

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import ru.itis.bookmatch.data.dao.LikedBookDao
import ru.itis.bookmatch.data.entity.LikedBookEntity
import ru.itis.bookmatch.data.toBookModel
import ru.itis.bookmatch.data.toLikedEntity
import ru.itis.bookmatch.domain.Book
import javax.inject.Inject

class LikedBooksRepositoryImpl @Inject constructor(
    context: Context,
    private val firestore: FirebaseFirestore,
    private val dao: LikedBookDao
): LikedBooksRepository {
    private val prefs = context.getSharedPreferences("syncTime", Context.MODE_PRIVATE)

    private fun getLastSyncTime(userId: String): Long {
        return prefs.getLong("liked_last_sync_time_${userId}", 0L)
    }

    private fun saveLastSyncTime(userId: String, time: Long) {
        prefs.edit().putLong("liked_last_sync_time_${userId}", time).apply()
    }

    override fun getLikedBooksFlow(userId: String): Flow<List<Book>> {
        return dao.getByUserId(userId).map { likedBookEntitiesList ->
            likedBookEntitiesList.map {
                it.toBookModel()
            }
        }
    }

    override suspend fun getLikedBook(
        userId: String,
        bookId: String
    ): Book {
        return (dao.getById(userId = userId, bookId = bookId))?.toBookModel() ?: throw Exception("Book not found")
    }

    override suspend fun addToLiked(userId: String, book: Book) {
        val entity = book.toLikedEntity(userId)
        dao.insert(entity)

        try {
            firestore.collection("users")
                .document(userId)
                .collection("liked_books")
                .document(entity.bookId)
                .set(entity)
                .await()
            dao.markAsSynced(userId, entity.bookId)
        } catch (e: Exception) {
        }
    }

    override suspend fun isLiked(userId: String, bookId: String): Boolean {
        return dao.getById(userId, bookId) != null
    }

    override suspend fun deleteFromLiked(userId: String, bookId: String) {
        dao.deleteById(userId, bookId)

        try {
            firestore.collection("users")
                .document(userId)
                .collection("liked_books")
                .document(bookId)
                .delete()
                .await()
        } catch (e: Exception) {

        }
    }

    override suspend fun syncWithFirestore(userId: String) {
        val lastSyncTime = getLastSyncTime(userId)

        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("liked_books")
                .whereGreaterThan("timestamp", lastSyncTime)
                .get()
                .await()
            val booksToSync = snapshot.toObjects(LikedBookEntity::class.java)


            booksToSync.forEach {
                dao.insert(it.copy(synced = true))
            }

            val unSyncedBooks = dao.getUnsynced(userId)
            unSyncedBooks.forEach {
                firestore.collection("users")
                    .document(userId)
                    .collection("liked_books")
                    .document(it.bookId)
                    .set(it)
                    .await()
                dao.markAsSynced(userId, it.bookId)
            }
            saveLastSyncTime(userId, System.currentTimeMillis())

        } catch (e: Exception) {
        }
    }

    override suspend fun getBooksCount(userId: String): Int {
        return dao.getBooksCount(userId)
    }
}