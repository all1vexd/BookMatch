package ru.itis.bookmatch.data.repository

import jakarta.inject.Inject
import ru.itis.bookmatch.data.dao.CachedBookDao
import ru.itis.bookmatch.data.toBookModel
import ru.itis.bookmatch.data.toCachedEntity
import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.repository.CachedBookRepository

class CachedBookRepositoryImpl @Inject constructor(
    private val dao: CachedBookDao
): CachedBookRepository {

    override suspend fun insert(userId: String, book: Book) {
        val bookEntity = book.toCachedEntity(userId)
        dao.insert(bookEntity)
    }

    override suspend fun getCachedBookById(
        userId: String,
        bookId: String
    ): Book? {
        return dao.getById(userId = userId, bookId = bookId)?.toBookModel()
    }

    override suspend fun deleteAll(userId: String) {
        dao.deleteAll(userId = userId)
    }
}