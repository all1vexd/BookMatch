package ru.itis.bookmatch.domain.likedUseCase

import ru.itis.bookmatch.data.repository.LikedBooksRepository

class RemoveFromLikedBooksUseCase(
    private val repository: LikedBooksRepository
) {
    suspend operator fun invoke(userId: String, bookId: String) {
        repository.deleteFromLiked(userId, bookId)
    }
}