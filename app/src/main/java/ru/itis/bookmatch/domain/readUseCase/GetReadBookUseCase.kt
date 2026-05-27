package ru.itis.bookmatch.domain.readUseCase

import ru.itis.bookmatch.data.repository.ReadBookRepository
import ru.itis.bookmatch.domain.ReadBook
import javax.inject.Inject

class GetReadBookUseCase @Inject constructor(
    private val readBookRepository: ReadBookRepository
) {

    suspend operator fun invoke(userId: String, bookId: String): ReadBook? {
        return readBookRepository.getById(userId, bookId)
    }
}