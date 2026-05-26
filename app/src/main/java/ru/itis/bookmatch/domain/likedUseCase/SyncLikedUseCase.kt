package ru.itis.bookmatch.domain.likedUseCase

import ru.itis.bookmatch.data.repository.LikedBooksRepository
import javax.inject.Inject

class SyncLikedUseCase @Inject constructor(
    private val repository: LikedBooksRepository
) {
    suspend operator fun invoke(userId: String) {
        repository.syncWithFirestore(userId)
    }
}