package ru.itis.bookmatch.domain.readUseCase

import ru.itis.bookmatch.data.repository.ReadBookRepository
import javax.inject.Inject

class SyncReadBooksUseCase @Inject constructor(
    private val readBookRepository: ReadBookRepository
) {

    suspend operator fun invoke(userId: String) {
        readBookRepository.syncWithFirestore(userId = userId)
    }
}