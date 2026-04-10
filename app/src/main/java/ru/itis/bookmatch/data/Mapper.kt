package ru.itis.bookmatch.data

import ru.itis.bookmatch.domain.Book

fun BookItem.toBook(): Book {
    return Book(
        id = this.id,
        title = this.volumeInfo.title ?: "Unknown Title",
        authors = this.volumeInfo.authors ?: emptyList(),
        description = this.volumeInfo.description ?: "",
        categories = this.volumeInfo.categories ?: emptyList(),
        publishedDate = this.volumeInfo.publishedDate ?: "",
        pageCount = this.volumeInfo.pageCount ?: 0,
        averageRating = this.volumeInfo.averageRating ?: 0.0,
        thumbnailUrl = this.volumeInfo.imageLinks?.thumbnail ?: "",
        smallThumbnailUrl = this.volumeInfo.imageLinks?.smallThumbnail ?: ""
    )
}

fun toHighQualityUrl(oldUrl: String): String {
    return oldUrl
        .takeIf { it.isNotEmpty() }
        ?.replace("http://", "https://")
        ?.replace("zoom=1", "zoom=3") ?: ""
}