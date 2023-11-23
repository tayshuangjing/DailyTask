package com.example.dailytask.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "title")
    var title: String?,
    @ColumnInfo(name = "content")
    var content: String?,
    @ColumnInfo(name = "date")
    var date: LocalDateTime?,
    @ColumnInfo(name = "username")
    var username: String?,
    @ColumnInfo(name = "status")
    var status: Boolean?
)