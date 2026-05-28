package ru.itis.bookmatch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.itis.bookmatch.data.entity.CachedBookEntity

@Dao
interface CachedBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CachedBookEntity)

    @Query("SELECT * FROM cached_book_entity WHERE userId = :userId AND bookId = :bookId")
    suspend fun getById(userId: String, bookId: String): CachedBookEntity?

    @Query("DELETE FROM cached_book_entity WHERE userId = :userId")
    suspend fun deleteAll(userId: String)
}