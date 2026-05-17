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
    val title: String? = null,
    @SerializedName("authors")
    val authors: List<String>? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("categories")
    val categories: List<String>? = null,
    @SerializedName("publishedDate")
    val publishedDate: String? = null,
    @SerializedName("pageCount")
    val pageCount: Int? = null,
    @SerializedName("averageRating")
    val averageRating: Double? = null,
    @SerializedName("imageLinks")
    val imageLinks: ImageLinks? = null
)

data class ImageLinks(
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
    @SerializedName("smallThumbnail")
    val smallThumbnail: String? = null
)