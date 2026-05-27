package ru.itis.bookmatch.domain.readUseCase

import ru.itis.bookmatch.data.repository.ReadBookRepository
import javax.inject.Inject

class GetReadBooksCountUseCase @Inject constructor(
    private val readBookRepository: ReadBookRepository
) {

    suspend operator fun invoke(userId: String): Int {
        return readBookRepository.getBooksCount(userId = userId)
    }
}