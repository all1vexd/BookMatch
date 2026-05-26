package ru.itis.bookmatch.domain.readUseCase

import ru.itis.bookmatch.data.repository.ReadBookRepository
import javax.inject.Inject

class UpdateRatingUseCase @Inject constructor(
    private val readBookRepository: ReadBookRepository
) {

    suspend operator fun invoke(userId: String, bookId: String, rating: Double) {
        readBookRepository.updateRating(userId = userId, bookId = bookId, rating = rating)
    }
}