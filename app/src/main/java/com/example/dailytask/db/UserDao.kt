package com.example.dailytask.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("UPDATE user_table SET role = :role WHERE username = :username")
    suspend fun updateRoleStatus(username: String, role: String)

    @Query("SELECT * FROM user_table ORDER BY username ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user_table WHERE username = :username")
    fun getUserByName(username: String): Flow<User?>

}