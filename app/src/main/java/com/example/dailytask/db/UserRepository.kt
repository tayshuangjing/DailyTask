package com.example.dailytask.db

import android.util.Log

class UserRepository(private val userDao: UserDao) {
    val allUser = userDao.getAllUsers()

    suspend fun insert(user: User){
        Log.d("User add", "${user.userName.toString()} and ${user.role}" )
        userDao.insert(user)
    }

    suspend fun updateRoleStatus(userId: Int, role: String){
        userDao.updateRoleStatus(userId,role)
    }
}