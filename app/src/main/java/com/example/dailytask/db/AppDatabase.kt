package com.example.dailytask.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.dailytask.util.Converters

@Database(
    entities = [
        Task::class,
        User::class
    ],
    version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao
    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }

            }
        }
        private fun buildDatabase(context: Context): AppDatabase{
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "task_database"
            ).build()
        }
    }
}