package ru.itis.bookmatch.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.itis.bookmatch.BuildConfig
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val BASE_URL = BuildConfig.GOOGLE_BOOKS_API_BASE_URL

    private val client = OkHttpClient.Builder()
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val googleBooksApi = retrofit.create(GoogleBooksApi::class.java)

    fun getGoogleBooksApi() = googleBooksApi

}