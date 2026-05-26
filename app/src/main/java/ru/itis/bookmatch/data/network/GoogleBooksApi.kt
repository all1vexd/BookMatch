package ru.itis.bookmatch.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.itis.bookmatch.BuildConfig
import ru.itis.bookmatch.data.GoogleBooksResponse

interface GoogleBooksApi {

    @GET("volumes")
    suspend fun getBookForSwipe(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("key") apiKey: String = BuildConfig.GOOGLE_BOOKS_API_KEY,
        @Query("startIndex") startIndex: Int = 0
    ): GoogleBooksResponse
}