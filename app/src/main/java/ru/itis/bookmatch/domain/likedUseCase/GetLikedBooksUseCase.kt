package ru.itis.bookmatch.domain.likedUseCase

import kotlinx.coroutines.flow.Flow
import ru.itis.bookmatch.data.repository.LikedBooksRepository
import ru.itis.bookmatch.domain.Book

class GetLikedBooksUseCase(
    private val repository: LikedBooksRepository
) {
    operator fun invoke(userId: String, ): Flow<List<Book>> {
        return repository.getLikedBooksFlow(userId)
    }
}