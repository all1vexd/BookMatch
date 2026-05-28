package ru.itis.bookmatch

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import ru.itis.bookmatch.data.dao.LikedBookDao
import ru.itis.bookmatch.data.dao.ReadBookDao
import ru.itis.bookmatch.data.network.GoogleBooksApi
import ru.itis.bookmatch.data.network.RetrofitClient
import ru.itis.bookmatch.domain.repository.AuthRepository
import ru.itis.bookmatch.data.repository.AuthRepositoryImpl
import ru.itis.bookmatch.data.repository.BookRepositoryImpl
import ru.itis.bookmatch.domain.repository.BookRepository
import ru.itis.bookmatch.domain.repository.LikedBooksRepository
import ru.itis.bookmatch.data.repository.LikedBooksRepositoryImpl
import ru.itis.bookmatch.domain.repository.ProfileRepository
import ru.itis.bookmatch.data.repository.ProfileRepositoryImpl
import ru.itis.bookmatch.domain.repository.ReadBookRepository
import ru.itis.bookmatch.data.repository.ReadBookRepositoryImpl
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): AuthRepository {
        return AuthRepositoryImpl(auth, firestore)
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

    @Provides
    @Singleton
    fun provideReadBookRepository(
        context: Context,
        firestore: FirebaseFirestore,
        dao: ReadBookDao
    ): ReadBookRepository {
        return ReadBookRepositoryImpl(context, firestore, dao)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(firestore: FirebaseFirestore): ProfileRepository {
        return ProfileRepositoryImpl(firestore)
    }
}