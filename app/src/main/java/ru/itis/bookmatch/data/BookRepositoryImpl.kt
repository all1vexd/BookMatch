package ru.itis.bookmatch.data

import android.util.Log
import ru.itis.bookmatch.data.network.RetrofitClient
import ru.itis.bookmatch.domain.Book

class BookRepositoryImpl: BookRepository {

    private val api = RetrofitClient.getGoogleBooksApi()

    override suspend fun getBookForSwipe(): List<Book> {
        val response = api.getBookForSwipe()
        Log.d("API_TEST", "Books count: ${response.items.size}")
        return response.items.map {
            it.toBook()
        }
    }

}