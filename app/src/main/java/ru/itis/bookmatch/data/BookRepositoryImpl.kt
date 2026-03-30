package ru.itis.bookmatch.data

import ru.itis.bookmatch.data.network.RetrofitClient
import ru.itis.bookmatch.domain.Book

class BookRepositoryImpl: BookRepository {

    private val api = RetrofitClient.getGoogleBooksApi()

    override suspend fun getBookForSwipe(): List<Book> {

        return api.getBookForSwipe().items.map {
            it.toBook()
        }
    }

}