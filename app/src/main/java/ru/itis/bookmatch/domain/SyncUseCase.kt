package ru.itis.bookmatch.domain

import ru.itis.bookmatch.data.repository.LikedBooksRepository

class SyncUseCase(
    private val repository: LikedBooksRepository
) {
    suspend operator fun invoke(userId: String) {
        repository.syncWithFirestore(userId)
    }
}