package com.example.dailytask.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("UPDATE user_table SET role = :role WHERE userId = :userId")
    suspend fun updateRoleStatus(userId: Int, role: String)

    @Query("SELECT * FROM user_table ORDER BY userId ASC")
    fun getAllUsers(): Flow<List<User>>
}