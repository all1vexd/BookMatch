package ru.itis.bookmatch.domain.cachedUseCase

import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.repository.CachedBookRepository
import javax.inject.Inject

class GetCachedBookUseCase @Inject constructor(
    private val cachedBookRepository: CachedBookRepository
) {

    suspend operator fun invoke(userId: String, bookId: String): Book? {
        return cachedBookRepository.getCachedBookById(userId = userId, bookId = bookId)
    }
}