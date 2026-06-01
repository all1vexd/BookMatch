package ru.itis.bookmatch.domain.likedUseCase

import ru.itis.bookmatch.domain.repository.LikedBooksRepository
import javax.inject.Inject

class IsLikedUseCase @Inject constructor(
    private val repository: LikedBooksRepository
) {
    suspend operator fun invoke(userId: String, bookId: String): Boolean {
        return repository.isLiked(userId, bookId)
    }
}