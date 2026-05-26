package ru.itis.bookmatch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.itis.bookmatch.data.entity.ReadBookEntity

@Dao
interface ReadBookDao {

    @Query("SELECT * FROM read_book WHERE userId = :userId ORDER BY timestamp DESC")
    fun getByUserId(userId: String): Flow<List<ReadBookEntity>>

    @Query("SELECT * FROM read_book WHERE bookId = :bookId AND userId = :userId")
    suspend fun getById(userId: String, bookId: String): ReadBookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ReadBookEntity)

    @Query("DELETE FROM read_book WHERE bookId = :bookId AND userId = :userId")
    suspend fun deleteById(userId: String, bookId: String)

    @Query("UPDATE read_book SET synced = 1 WHERE userId = :userId AND bookId = :bookId")
    suspend fun markAsSynced(userId: String, bookId: String)

    @Query("SELECT * FROM read_book WHERE userId = :userId AND synced = 0")
    suspend fun getUnsynced(userId: String): List<ReadBookEntity>

    @Query("UPDATE read_book SET feedBack = :feedback WHERE userId = :userId AND bookId = :bookId")
    suspend fun updateFeedback(bookId: String, userId: String, feedback: String)

    @Query("UPDATE read_book SET readerRating = :rating WHERE userId = :userId AND bookId = :bookId")
    suspend fun updateRating(userId: String, bookId: String, rating: Double)
}