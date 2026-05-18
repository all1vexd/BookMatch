package ru.itis.bookmatch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.itis.bookmatch.data.dao.LikedBookDao
import ru.itis.bookmatch.data.entity.LikedBookEntity
import ru.itis.bookmatch.domain.Book

@Database(
    entities = [LikedBookEntity::class],
    version = 2,
    exportSchema = false
)
abstract class BookDatabase: RoomDatabase() {

    abstract fun likedBookDao(): LikedBookDao

    companion object {

        private var instance: BookDatabase? = null
        private var LOCK = Any()

        fun getInstance(context: Context): BookDatabase {

            instance?.let {
                return it
            }

            synchronized(LOCK) {
                instance?.let {
                    return it
                }

                return Room.databaseBuilder(
                    context = context,
                    klass = BookDatabase::class.java,
                    name = "book_database"
                ).fallbackToDestructiveMigration().build().also {
                    instance = it
                }
            }
        }
    }
}