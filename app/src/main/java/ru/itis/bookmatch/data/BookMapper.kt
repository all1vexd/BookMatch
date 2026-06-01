package ru.itis.bookmatch.data

import com.google.gson.Gson
import ru.itis.bookmatch.data.entity.CachedBookEntity
import ru.itis.bookmatch.data.entity.LikedBookEntity
import ru.itis.bookmatch.data.entity.ReadBookEntity
import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.ReadBook
import javax.inject.Inject

class BookMapper @Inject constructor(private val gson: Gson) {

    fun fromLikedEntity(entity: LikedBookEntity): Book =
        gson.fromJson(entity.bookJson, Book::class.java)

    fun toLikedEntity(book: Book, userId: String): LikedBookEntity =
        LikedBookEntity(
            id = "${userId}_${book.id}",
            userId = userId,
            bookId = book.id,
            bookJson = gson.toJson(book)
        )

    fun fromReadEntity(entity: ReadBookEntity): ReadBook =
        ReadBook(
            book = gson.fromJson(entity.bookJson, Book::class.java),
            rating = entity.readerRating,
            feedback = entity.feedBack
        )

    fun toReadEntity(readBook: ReadBook, userId: String): ReadBookEntity =
        ReadBookEntity(
            id = "${userId}_${readBook.book.id}",
            userId = userId,
            bookId = readBook.book.id,
            bookJson = gson.toJson(readBook.book),
            feedBack = readBook.feedback,
            readerRating = readBook.rating
        )

    fun fromCachedEntity(entity: CachedBookEntity): Book =
        gson.fromJson(entity.bookJson, Book::class.java)

    fun toCachedEntity(book: Book, userId: String): CachedBookEntity =
        CachedBookEntity(
            id = "${userId}_${book.id}",
            userId = userId,
            bookId = book.id,
            bookJson = gson.toJson(book)
        )
}
