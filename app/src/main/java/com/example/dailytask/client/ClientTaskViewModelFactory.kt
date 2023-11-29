package com.example.dailytask.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dailytask.db.TaskRepository

class ClientTaskViewModelFactory(private val repository: TaskRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientTaskViewModel::class.java)){
            return ClientTaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}