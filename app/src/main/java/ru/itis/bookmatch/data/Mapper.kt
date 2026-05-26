package ru.itis.bookmatch.data

import com.google.gson.Gson
import ru.itis.bookmatch.data.entity.LikedBookEntity
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
        thumbnailUrl = toHighQualityUrl(this.volumeInfo.imageLinks?.thumbnail ?: ""),
        smallThumbnailUrl = toHighQualityUrl(this.volumeInfo.imageLinks?.smallThumbnail ?: "")
    )
}

fun toHighQualityUrl(oldUrl: String): String {
    return oldUrl
        .takeIf { it.isNotEmpty() }
        ?.replace("http://", "https://") ?: ""
}

fun LikedBookEntity.toBookModel(): Book {
    return Gson().fromJson(this.bookJson, Book::class.java)
}

fun Book.toLikedEntity(userId: String): LikedBookEntity {
    return LikedBookEntity(
        id = "${userId}_${this.id}",
        userId = userId,
        bookId = this.id,
        bookJson = Gson().toJson(this)
    )
}