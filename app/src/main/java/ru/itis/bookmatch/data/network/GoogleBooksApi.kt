package ru.itis.bookmatch.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.itis.bookmatch.BuildConfig
import ru.itis.bookmatch.data.GoogleBooksResponse

interface GoogleBooksApi {

    @GET("volumes")
    suspend fun getBookForSwipe(
        @Query("q") query: String = "subject:fiction",
        @Query("maxResults") maxResults: Int = 20,
        @Query("key") apiKey: String = BuildConfig.GOOGLE_BOOKS_API_KEY
    ): GoogleBooksResponse
}