package ru.itis.bookmatch.domain.likedUseCase

import ru.itis.bookmatch.data.repository.LikedBooksRepository
import ru.itis.bookmatch.domain.Book
import javax.inject.Inject

class AddToLikedUseCase @Inject constructor(
    private val repository: LikedBooksRepository
) {
    suspend operator fun invoke(userId: String, book: Book) {
        repository.addToLiked(userId, book)
    }
}