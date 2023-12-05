package com.example.dailytask.user

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.dailytask.admin.AdminTaskViewModel
import com.example.dailytask.db.User
import com.example.dailytask.db.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository): ViewModel(){

    val user = repository.allUser
    fun insert(user: User) =
        viewModelScope.launch {
            repository.insert(user)
        }

    fun getAllUsers() = liveData {
        user.collect{
            emit(it)
        }
    }
}

class UserViewModelFactory(private val repository: UserRepository): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modeClass: Class<T>): T{
        if(modeClass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}