package com.example.dailytask.client

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.dailytask.databinding.ActivityAddClientBinding
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskDatabase
import com.example.dailytask.db.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ClientTaskViewModel(private val repository: TaskRepository): ViewModel() {

    val tasks = repository.allTasks

    val existingNames = mutableListOf("John", "Jane", "Alice", "Bob", "Charlie")
    val rvNames = mutableListOf<String>()

    fun insert(task: Task) = viewModelScope.launch {
        Log.i("mytag", "data is inserted: $task")
        repository.insert(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun delete(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }

    fun deleteTaskById(taskId: Int) = viewModelScope.launch {
        repository.deleteTaskById(taskId)
    }

    fun getAllTasks() = liveData {
        tasks.collect{
            emit(it)
            Log.i("mytag", "tasks in view model: $it")
        }
    }

    fun getTaskById(taskId: Int): Flow<Task?> {
        return repository.getTaskById(taskId)
    }
}