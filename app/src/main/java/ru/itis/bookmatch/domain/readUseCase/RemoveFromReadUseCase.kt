package ru.itis.bookmatch.domain.readUseCase

import javax.inject.Inject
import ru.itis.bookmatch.domain.repository.ReadBookRepository

class RemoveFromReadUseCase @Inject constructor(
    private val readBookRepository: ReadBookRepository
) {

    suspend operator fun invoke(userId: String, bookId: String) {
        readBookRepository.deleteFromRead(userId = userId, bookId = bookId)
    }
}