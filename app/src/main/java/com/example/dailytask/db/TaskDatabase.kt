package com.example.dailytask.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.dailytask.util.Converters

@Database(entities = arrayOf(Task::class), version = 1)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract val taskDao : TaskDao

    companion object{
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }

            }
        }
        private fun buildDatabase(context: Context): TaskDatabase{
            return Room.databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                "task_database"
            ).build()
        }
    }
}