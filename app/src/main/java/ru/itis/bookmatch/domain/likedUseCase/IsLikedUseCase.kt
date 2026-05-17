package ru.itis.bookmatch.domain.likedUseCase

import ru.itis.bookmatch.data.repository.LikedBooksRepository

class IsLikedUseCase(
    private val repository: LikedBooksRepository
) {
    suspend operator fun invoke(userId: String, bookId: String): Boolean {
        return repository.isLiked(userId, bookId)
    }
}