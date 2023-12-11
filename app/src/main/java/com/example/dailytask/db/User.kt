package com.example.dailytask.db

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "userId")
    var userId: String = "0001",
    @ColumnInfo(name = "username")
    var userName: String,
    @ColumnInfo(name = "password")
    var password: String?,
    @ColumnInfo(name = "role")
    var role: String
)
