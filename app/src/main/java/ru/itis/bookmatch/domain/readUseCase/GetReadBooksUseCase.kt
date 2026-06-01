package ru.itis.bookmatch.domain.readUseCase


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.itis.bookmatch.domain.repository.ReadBookRepository
import ru.itis.bookmatch.domain.ReadBook
import javax.inject.Inject

class GetReadBooksUseCase @Inject constructor(
    private val readBooksRepository: ReadBookRepository
) {

    operator fun invoke(userId: String): Flow<List<ReadBook>> {
        return readBooksRepository.getReadBooksFlow(userId = userId)
    }
}