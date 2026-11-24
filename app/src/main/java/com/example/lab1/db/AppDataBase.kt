package com.example.lab1.db

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.example.lab1.dao.AppDao
import com.example.lab1.entity.LectureEntity
import com.example.lab1.entity.TutorEntity

@Database(
    entities = [TutorEntity::class, LectureEntity::class],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder<AppDataBase>(
                    context.applicationContext,
                    "app_database.db3"
                )
                    .setDriver(AndroidSQLiteDriver())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}