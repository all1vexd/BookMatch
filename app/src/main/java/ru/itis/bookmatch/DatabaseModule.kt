package ru.itis.bookmatch

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import ru.itis.bookmatch.data.BookDatabase
import ru.itis.bookmatch.data.dao.CachedBookDao
import ru.itis.bookmatch.data.dao.LikedBookDao
import ru.itis.bookmatch.data.dao.ReadBookDao
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideBookDataBase(context: Context): BookDatabase {
        return BookDatabase.getInstance(context)
    }

    @Provides
    fun provideLikedBookDao(database: BookDatabase): LikedBookDao {
        return database.likedBookDao()
    }

    @Provides
    fun provideReadBookDao(database: BookDatabase): ReadBookDao {
        return database.readBookDao()
    }

    @Provides
    fun provideCachedBookDao(database: BookDatabase): CachedBookDao {
        return database.cachedBookDao()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}