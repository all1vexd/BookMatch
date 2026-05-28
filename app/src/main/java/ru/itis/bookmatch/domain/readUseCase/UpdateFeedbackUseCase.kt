package ru.itis.bookmatch.domain.readUseCase

import ru.itis.bookmatch.domain.repository.ReadBookRepository
import javax.inject.Inject

class UpdateFeedbackUseCase @Inject constructor(
    private val readBookRepository: ReadBookRepository
) {

    suspend operator fun invoke(userId: String, bookId: String, feedback: String) {
        readBookRepository.updateFeedback(userId = userId, bookId = bookId, feedback = feedback)
    }
}