package ru.itis.bookmatch

import dagger.Module
import dagger.Provides
import ru.itis.bookmatch.data.network.GoogleBooksApi
import ru.itis.bookmatch.data.network.RetrofitClient
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesGoogleBookApi(): GoogleBooksApi {
        return RetrofitClient.getGoogleBooksApi()
    }
}