package ru.itis.bookmatch.domain.likedUseCase

import ru.itis.bookmatch.data.repository.LikedBooksRepository
import javax.inject.Inject

class RemoveFromLikedBooksUseCase @Inject constructor(
    private val repository: LikedBooksRepository
) {
    suspend operator fun invoke(userId: String, bookId: String) {
        repository.deleteFromLiked(userId, bookId)
    }
}