package ru.itis.bookmatch.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_book_entity")
data class CachedBookEntity(
    @PrimaryKey
    val id: String = "",
    val userId: String = "",
    val bookId: String = "",
    val bookJson: String = ""
)