package com.example.dailytask.util

import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromTimeStamp(value: String?): LocalDateTime?{
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimeStamp(date: LocalDateTime?):String?{
        return date?.toString()
    }
}