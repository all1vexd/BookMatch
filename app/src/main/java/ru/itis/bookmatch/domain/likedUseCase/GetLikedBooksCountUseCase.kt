package ru.itis.bookmatch.domain.likedUseCase

import ru.itis.bookmatch.domain.repository.LikedBooksRepository
import javax.inject.Inject

class GetLikedBooksCountUseCase @Inject constructor(
    private val likedBooksRepository: LikedBooksRepository
) {

    suspend operator fun invoke(userId: String): Int {
        return likedBooksRepository.getBooksCount(userId)
    }
}
