package ru.itis.bookmatch.domain.readUseCase

import ru.itis.bookmatch.data.repository.ReadBookRepository
import ru.itis.bookmatch.domain.ReadBook
import javax.inject.Inject

class IsReadUseCase @Inject constructor(
    private val readBooksRepository: ReadBookRepository
) {
    suspend operator fun invoke(userId: String, bookId: String) {
        readBooksRepository.isRead(userId = userId, bookId = bookId)
    }
}