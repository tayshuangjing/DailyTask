package com.example.dailytask.client

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskDatabase
import com.example.dailytask.db.TaskRepository
import kotlinx.coroutines.launch

class ClientTaskViewModel(private val repository: TaskRepository): ViewModel() {


    val tasks = repository.allTasks

    fun insert(task: Task) = viewModelScope.launch {
        Log.i("MyTag", "data inserted")
        repository.insert(task)
    }

    fun getAllTasks() = liveData {
        tasks.collect{
            emit(it)
        }
    }
}