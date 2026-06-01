package ru.itis.bookmatch

import dagger.Binds
import dagger.Module
import ru.itis.bookmatch.data.repository.AuthRepositoryImpl
import ru.itis.bookmatch.data.repository.BookRepositoryImpl
import ru.itis.bookmatch.data.repository.CachedBookRepositoryImpl
import ru.itis.bookmatch.data.repository.LikedBooksRepositoryImpl
import ru.itis.bookmatch.data.repository.ProfileRepositoryImpl
import ru.itis.bookmatch.data.repository.ReadBookRepositoryImpl
import ru.itis.bookmatch.domain.repository.AuthRepository
import ru.itis.bookmatch.domain.repository.BookRepository
import ru.itis.bookmatch.domain.repository.CachedBookRepository
import ru.itis.bookmatch.domain.repository.LikedBooksRepository
import ru.itis.bookmatch.domain.repository.ProfileRepository
import ru.itis.bookmatch.domain.repository.ReadBookRepository
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds @Singleton
    abstract fun bindBookRepository(impl: BookRepositoryImpl): BookRepository

    @Binds @Singleton
    abstract fun bindLikedBooksRepository(impl: LikedBooksRepositoryImpl): LikedBooksRepository

    @Binds @Singleton
    abstract fun bindReadBookRepository(impl: ReadBookRepositoryImpl): ReadBookRepository

    @Binds @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds @Singleton
    abstract fun bindCachedBookRepository(impl: CachedBookRepositoryImpl): CachedBookRepository

}
