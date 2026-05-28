package ru.itis.bookmatch.domain.cachedUseCase

import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.repository.CachedBookRepository
import javax.inject.Inject

class AddCachedBookUseCase @Inject constructor(
    private val cachedBookRepository: CachedBookRepository
) {

    suspend operator fun invoke(userId: String, book: Book) {
        cachedBookRepository.insert(userId = userId, book = book)
    }
}