package ru.itis.bookmatch.data

import com.google.gson.Gson
import ru.itis.bookmatch.data.dao.CachedBookDao
import ru.itis.bookmatch.data.entity.CachedBookEntity
import ru.itis.bookmatch.data.entity.LikedBookEntity
import ru.itis.bookmatch.data.entity.ReadBookEntity
import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.ReadBook

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

fun ReadBookEntity.toBookModel(): ReadBook {
    return ReadBook(
        book = Gson().fromJson(this.bookJson, Book::class.java),
        rating = this.readerRating,
        feedback = this.feedBack
    )
}

fun ReadBook.toReadEntity(userId: String): ReadBookEntity {
    return ReadBookEntity(
        id = "${userId}_${this.book.id}",
        userId = userId,
        bookId = this.book.id,
        bookJson = Gson().toJson(this.book),
        feedBack = this.feedback,
        readerRating = this.rating
    )
}

fun CachedBookEntity.toBookModel(): Book {
    return Gson().fromJson(this.bookJson, Book::class.java)
}

fun Book.toCachedEntity(userId: String): CachedBookEntity {
    return CachedBookEntity(
        id = "${userId}_${this.id}",
        userId = userId,
        bookId = this.id,
        bookJson = Gson().toJson(this)
    )
}