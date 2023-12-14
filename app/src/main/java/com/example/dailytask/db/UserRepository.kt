package com.example.dailytask.db

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {
    val allUser = userDao.getAllUsers()

    suspend fun insert(user: User){
        Log.d("User add", "${user.userId} and ${user.username}" )
        userDao.insert(user)
    }

    suspend fun delete(user: User): Int{
        return userDao.delete(user)
    }

    fun getUserByID(userId: String): Flow<User?> {
        Log.d("UserRepository", "Querying username: $userId")
        return userDao.getUserByID(userId).onEach { user: User? ->
            Log.d("UserRepository", "User result: $user")
        }
    }

suspend fun getLastUserIdByRole(role: String): String? {
            return withContext(Dispatchers.IO) {
                userDao.getLastUserIdByRole(role)
            }
        }


}