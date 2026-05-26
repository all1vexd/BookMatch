package ru.itis.bookmatch

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import ru.itis.bookmatch.data.dao.LikedBookDao
import ru.itis.bookmatch.data.network.GoogleBooksApi
import ru.itis.bookmatch.data.network.RetrofitClient
import ru.itis.bookmatch.data.repository.AuthRepository
import ru.itis.bookmatch.data.repository.AuthRepositoryImpl
import ru.itis.bookmatch.data.repository.BookRepositoryImpl
import ru.itis.bookmatch.domain.BookRepository
import ru.itis.bookmatch.data.repository.LikedBooksRepository
import ru.itis.bookmatch.data.repository.LikedBooksRepositoryImpl
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Provides
    @Singleton
    fun provideLikedBooksRepository(
        context: Context,
        firestore: FirebaseFirestore,
        dao: LikedBookDao
    ): LikedBooksRepository {
        return LikedBooksRepositoryImpl(context, firestore, dao)
    }

    @Provides
    @Singleton
    fun provideGoogleBooksApi(): GoogleBooksApi {
        return RetrofitClient.getGoogleBooksApi()
    }

    @Provides
    @Singleton
    fun provideBookRepository(api: GoogleBooksApi): BookRepository {
        return BookRepositoryImpl(api)
    }
}