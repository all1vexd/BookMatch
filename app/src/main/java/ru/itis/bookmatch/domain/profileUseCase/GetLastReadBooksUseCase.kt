package ru.itis.bookmatch.domain.profileUseCase

import kotlinx.coroutines.flow.Flow
import ru.itis.bookmatch.data.repository.ReadBookRepository
import ru.itis.bookmatch.domain.ReadBook
import javax.inject.Inject

class GetLastReadBooksUseCase @Inject constructor(
    private val readBookRepository: ReadBookRepository
) {

    suspend operator fun invoke(userId: String): Flow<List<ReadBook>> {
        return readBookRepository.getLastReadBook(userId = userId)
    }
}