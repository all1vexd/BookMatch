package ru.itis.bookmatch.domain.likedUseCase

import ru.itis.bookmatch.data.repository.LikedBooksRepository
import ru.itis.bookmatch.domain.Book

class AddToLikedUseCase(
    private val repository: LikedBooksRepository
) {
    suspend operator fun invoke(userId: String, book: Book) {
        repository.addToLiked(userId, book)
    }
}