package com.example.dailytask.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User): Int

    @Query("SELECT * FROM user_table ORDER BY userId ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user_table WHERE userId = :userId")
    fun getUserByID(userId: String): Flow<User?>

    @Query("SELECT userId FROM user_table WHERE role = :role ORDER BY userId DESC LIMIT 1")
    fun getLastUserIdByRole(role: String): String?


}