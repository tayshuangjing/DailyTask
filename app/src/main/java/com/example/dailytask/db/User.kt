package com.example.dailytask.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userId")
    val userId: Int?,
    @ColumnInfo(name = "username")
    var userName: String,
    @ColumnInfo(name = "password")
    var password: String?,
    @ColumnInfo(name = "role")
    var role: String
)