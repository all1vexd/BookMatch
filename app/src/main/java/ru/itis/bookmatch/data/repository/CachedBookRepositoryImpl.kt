package ru.itis.bookmatch.data.repository

import javax.inject.Inject
import ru.itis.bookmatch.data.BookMapper
import ru.itis.bookmatch.data.dao.CachedBookDao
import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.repository.CachedBookRepository

class CachedBookRepositoryImpl @Inject constructor(
    private val dao: CachedBookDao,
    private val mapper: BookMapper
): CachedBookRepository {

    override suspend fun insert(userId: String, book: Book) {
        val bookEntity = mapper.toCachedEntity(book, userId)
        dao.insert(bookEntity)
    }

    override suspend fun getCachedBookById(
        userId: String,
        bookId: String
    ): Book? {
        return dao.getById(userId = userId, bookId = bookId)?.let { mapper.fromCachedEntity(it) }
    }

    override suspend fun deleteAll(userId: String) {
        dao.deleteAll(userId = userId)
    }
}