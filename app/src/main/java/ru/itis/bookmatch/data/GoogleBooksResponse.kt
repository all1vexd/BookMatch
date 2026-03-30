package ru.itis.bookmatch.data

import com.google.gson.annotations.SerializedName

data class GoogleBooksResponse (
    @SerializedName("kind")
    val kind: String,
    @SerializedName("totalItems")
    val totalItems: Long,
    @SerializedName("items")
    val items: List<BookItem>
)

data class BookItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("volumeInfo")
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    @SerializedName("title")
    val title: String,
    @SerializedName("authors")
    val authors: List<String>,
    @SerializedName("description")
    val description: String,
    @SerializedName("categories")
    val categories: List<String>,
    @SerializedName("publishedDate")
    val publishedDate: String,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("averageRating")
    val averageRating: Double,
    @SerializedName("imageLinks")
    val imageLinks: ImageLinks
)

data class ImageLinks(
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("smallThumbnail")
    val smallThumbnail: String
)