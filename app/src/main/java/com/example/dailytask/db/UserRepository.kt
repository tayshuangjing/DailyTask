package com.example.dailytask.db

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class UserRepository(private val userDao: UserDao) {
    val allUser = userDao.getAllUsers()

    suspend fun insert(user: User){
        Log.d("User add", "${user.userName} and ${user.role}" )
        userDao.insert(user)
    }

    suspend fun updateRoleStatus(username: String, role: String){
        userDao.updateRoleStatus(username,role)
    }

    fun getUserByName(userName: String): Flow<User?> {
        Log.d("UserRepository", "Querying username: $userName")
        return userDao.getUserByName(userName).onEach { user: User? ->
            Log.d("UserRepository", "User result: $user")
        }
    }
}