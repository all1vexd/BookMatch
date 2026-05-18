package ru.itis.bookmatch.data.repository

import ru.itis.bookmatch.data.network.RetrofitClient
import ru.itis.bookmatch.data.toBook
import ru.itis.bookmatch.domain.Book

class BookRepositoryImpl: BookRepository {

    private val api = RetrofitClient.getGoogleBooksApi()

    override suspend fun getBookForSwipe(): List<Book> {
        val response = api.getBookForSwipe()
        return response.items.map {
            it.toBook()
        }
    }

}