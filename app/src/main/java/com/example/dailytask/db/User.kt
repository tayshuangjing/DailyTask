package com.example.dailytask.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "userId")
    var userId: String = "null",
    @ColumnInfo(name = "username")
    var userName: String,
    @ColumnInfo(name = "password")
    var password: String?,
    @ColumnInfo(name = "role")
    var role: String
) {
    init {
        // Generate the custom ID when the User object is created
        userId = if (role == "admin") {
            "A" + generateNextId("admin")
        } else if (role == "client") {
            "C" + generateNextId("client")
        } else {
            userId
        }
    }

    private fun generateNextId(role: String): String {
        val numericPart = (userId.substring(1).toIntOrNull() ?: 0) + 1
        return role + numericPart.toString().padStart(3, '0')
    }
}