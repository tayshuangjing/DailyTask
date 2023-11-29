package com.example.dailytask.client

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskRepository
import kotlinx.coroutines.launch

class ClientTaskViewModel(private val repository: TaskRepository): ViewModel() {

    private val taskList = MutableLiveData<List<Task>>()
    val allTasks : LiveData<List<Task>> = taskList

    fun insert(task: Task) = viewModelScope.launch {
        Log.i("MyTag", "data inserted")
        repository.insert(task)
    }
}