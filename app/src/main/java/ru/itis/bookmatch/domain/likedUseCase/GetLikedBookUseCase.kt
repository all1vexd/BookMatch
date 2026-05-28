package ru.itis.bookmatch.domain.likedUseCase

import ru.itis.bookmatch.domain.repository.LikedBooksRepository
import ru.itis.bookmatch.domain.Book
import javax.inject.Inject

class GetLikedBookUseCase @Inject constructor(
    private val likedBooksRepository: LikedBooksRepository
) {

    suspend operator fun invoke(userId: String, bookId: String): Book? {
        return likedBooksRepository.getLikedBook(userId = userId, bookId = bookId)
    }
}