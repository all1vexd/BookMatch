package ru.itis.bookmatch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.itis.bookmatch.data.entity.LikedBookEntity
import ru.itis.bookmatch.domain.Book

@Dao
interface LikedBookDao {

    @Query("SELECT * FROM liked_books WHERE userId = :userId ORDER BY timestamp DESC")
    fun getByUserId(userId: String): Flow<List<LikedBookEntity>>

    @Query("SELECT * FROM liked_books WHERE bookId = :bookId AND userId = :userId")
    suspend fun getById(userId: String, bookId: String): LikedBookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: LikedBookEntity)

    @Query("DELETE FROM liked_books WHERE bookId = :bookId AND userId = :userId")
    suspend fun deleteById(userId: String, bookId: String)

    @Query("UPDATE liked_books SET synced = 1 WHERE userId = :userId AND bookId = :bookId")
    suspend fun markAsSynced(userId: String, bookId: String)

    @Query("SELECT * FROM liked_books WHERE userId = :userId AND synced = 0")
    suspend fun getUnsynced(userId: String): List<LikedBookEntity>
}