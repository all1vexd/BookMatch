package ru.itis.bookmatch.data

import ru.itis.bookmatch.domain.Book

fun BookItem.toBook(): Book {
    return Book(
        id = this.id,
        title = this.volumeInfo.title,
        authors = this.volumeInfo.authors,
        description = this.volumeInfo.description,
        categories = this.volumeInfo.categories,
        publishedDate = this.volumeInfo.publishedDate,
        pageCount = this.volumeInfo.pageCount,
        averageRating = this.volumeInfo.averageRating,
        thumbnailUrl = this.volumeInfo.imageLinks.thumbnail,
        smallThumbnailUrl = this.volumeInfo.imageLinks.smallThumbnail
    )
}