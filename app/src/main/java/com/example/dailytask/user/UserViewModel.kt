package com.example.dailytask.user

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.dailytask.admin.AdminTaskViewModel
import com.example.dailytask.db.Task
import com.example.dailytask.db.User
import com.example.dailytask.db.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val repository: UserRepository): ViewModel(){
    val user = repository.allUser

    fun insert(user: User) =
        viewModelScope.launch {
            repository.insert(user)
        }


    fun getAllUsers() = liveData {
        user.collect{
            emit(it)
            Log.d("User", it.toString())
        }
    }

    fun getUserByID(userId: String): Flow<User?> {
        return repository.getUserByID(userId)
    }

    fun getLastUserIdByRole(role: String): LiveData<String?> {
        val result = MutableLiveData<String?>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val userId = repository.getLastUserIdByRole(role)
                result.postValue(userId)
            }
        }
        return result
    }


    fun delete(user: User) = viewModelScope.launch {
        val deleteUser = repository.delete(user)
        if(deleteUser > 0){
            Log.d("Delete successful", deleteUser.toString())
        }else{
            Log.d("Error", "Error Occurred")
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