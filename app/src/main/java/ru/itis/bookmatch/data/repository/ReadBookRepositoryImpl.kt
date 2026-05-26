package ru.itis.bookmatch.data.repository

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import ru.itis.bookmatch.data.dao.ReadBookDao
import ru.itis.bookmatch.data.entity.ReadBookEntity
import ru.itis.bookmatch.data.toBookModel
import ru.itis.bookmatch.data.toReadEntity
import ru.itis.bookmatch.domain.ReadBook
import javax.inject.Inject

class ReadBookRepositoryImpl @Inject constructor(
    context: Context,
    private val firestore: FirebaseFirestore,
    private val dao: ReadBookDao
): ReadBookRepository {

    private val prefs = context.getSharedPreferences("syncTime", Context.MODE_PRIVATE)

    private fun getLastSyncTime(userId: String): Long {
        return prefs.getLong("read_last_sync_time_${userId}", 0L)
    }

    private fun saveLastSyncTime(userId: String, time: Long) {
        prefs.edit().putLong("read_last_sync_time_${userId}", time).apply()
    }

    override fun getReadBooksFlow(userId: String): Flow<List<ReadBook>> {
        return dao.getByUserId(userId).map { entityList ->
            entityList.map {
                it.toBookModel()
            }
        }
    }

    override suspend fun addToRead(userId: String, readBook: ReadBook) {
        val entity = readBook.toReadEntity(userId)
        dao.insert(entity)
    }

    override suspend fun isRead(userId: String, bookId: String): Boolean {
        return (dao.getById(userId = userId, bookId = bookId) != null)
    }

    override suspend fun deleteFromRead(userId: String, bookId: String) {
        return dao.deleteById(userId = userId, bookId = bookId)
    }

    override suspend fun updateFeedback(
        userId: String,
        bookId: String,
        feedback: String
    ) {
        dao.updateFeedback(userId = userId, bookId = bookId, feedback = feedback)
    }

    override suspend fun updateRating(userId: String, bookId: String, rating: Double) {
        dao.updateRating(userId = userId, bookId = bookId, rating = rating)
    }

    override suspend fun syncWithFirestore(userId: String) {
        val lastSyncTime = getLastSyncTime(userId)

        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("read_books")
                .whereGreaterThan("timestamp", lastSyncTime)
                .get()
                .await()
            val bookToSync = snapshot.toObjects(ReadBookEntity::class.java)

            bookToSync.forEach {
                dao.insert(it.copy(synced = true))
            }

            val unsyncedBooks = dao.getUnsynced(userId)
            unsyncedBooks.forEach {
                firestore.collection("users")
                    .document(userId)
                    .collection("read_books")
                    .document(it.bookId)
                    .set(it)
                    .await()
                dao.markAsSynced(userId, it.bookId)
            }
            saveLastSyncTime(userId = userId, time = System.currentTimeMillis())
        } catch (e: Exception) {
        }
    }
}