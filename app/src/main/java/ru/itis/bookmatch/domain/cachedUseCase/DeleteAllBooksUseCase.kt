package ru.itis.bookmatch.domain.cachedUseCase

import ru.itis.bookmatch.domain.repository.CachedBookRepository
import javax.inject.Inject

class DeleteAllBooksUseCase @Inject constructor(
    private val cachedBookRepository: CachedBookRepository
) {

    suspend operator fun invoke(userId: String) {
        cachedBookRepository.deleteAll(userId = userId)
    }
}