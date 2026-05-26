package ru.itis.bookmatch.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "read_book")
data class ReadBookEntity(
    @PrimaryKey
    val id: String = "",
    val userId: String = "",
    val bookId: String = "",
    val bookJson: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val synced: Boolean = false,
    val feedBack: String = "",
    val readerRating: Double = 0.0,
)