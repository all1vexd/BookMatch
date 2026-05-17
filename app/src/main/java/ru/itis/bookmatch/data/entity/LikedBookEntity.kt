package ru.itis.bookmatch.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked_books")
data class LikedBookEntity(
    @PrimaryKey
    val id: String = "",
    val userId: String = "",
    val bookId: String = "",
    val bookJson: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)