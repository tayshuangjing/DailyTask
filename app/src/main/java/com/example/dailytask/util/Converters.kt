package com.example.dailytask.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
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

    @TypeConverter
    fun fromString(value: String?): MutableList<String?>? {
        val listType: Type = object : TypeToken<MutableList<String?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: MutableList<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}