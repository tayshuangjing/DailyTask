package com.example.dailytask.client

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskRepository
import com.example.dailytask.db.TaskWithUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ClientTaskViewModel(private val repository: TaskRepository): ViewModel() {

    val tasks = repository.allTasks

    val existingNames = mutableListOf("John", "Jane", "Alice", "Bob", "Charlie")

    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun deleteTaskById(taskId: Int) = viewModelScope.launch {
        repository.deleteTaskById(taskId)
    }

    fun getAllTasks() = liveData {
        tasks.collect{
            emit(it)
        }
    }

    fun getTaskById(taskId: Int): Flow<Task?> {
        return repository.getTaskById(taskId)
    }

    fun getTasksByUsername(username: String) = liveData {
        repository.getTasksByUsername(username).collect{
            emit(it)
        }
    }
}

class ClientTaskViewModelFactory(private val repository: TaskRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientTaskViewModel::class.java)){
            return ClientTaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
