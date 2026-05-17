package ru.itis.bookmatch.domain

data class Book (
    val id: String,
    val title: String,
    val authors: List<String>,
    val description: String,
    val categories: List<String>,
    val publishedDate: String,
    val pageCount: Int,
    val averageRating: Double,
    val thumbnailUrl: String,
    val smallThumbnailUrl: String
)