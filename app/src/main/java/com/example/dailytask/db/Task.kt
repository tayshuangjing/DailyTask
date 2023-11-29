package com.example.dailytask.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "task_table")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "title")
    var title: String?,
    @ColumnInfo(name = "content")
    var content: String?,
    @ColumnInfo(name = "date")
    val date: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "username")
    var username: String?,
    @ColumnInfo(name = "status")
    var status: Boolean?
): Parcelable{
    val createDateFormat: String
        get() = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
}