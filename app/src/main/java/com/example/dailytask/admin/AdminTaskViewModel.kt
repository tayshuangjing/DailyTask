package com.example.dailytask.admin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AdminTaskViewModel(private val repository: TaskRepository): ViewModel() {

    val task = repository.allTasks

    fun getTaskById(taskId: Int): Flow<Task?> {
        return repository.getTaskById(taskId)
    }

    fun getAllTasks() = liveData {
        task.collect{
            emit(it)
            Log.d("Task",it.toString())
        }
    }

    fun searchTask(query:String):LiveData<List<Task>>{
        return liveData {
            val filteredList = repository.allTasks.first().filter { task: Task ->
                task.title!!.contains(query,ignoreCase = true)
//                ||task.content!!.contains(query,ignoreCase = true)
                ||task.createDateFormat!!.contains(query,ignoreCase = true)
                ||task.username!!.contains(query,ignoreCase = true)
            }
            emit(filteredList)
        }
    }

    fun updateTaskStatus(taskId: Int, newStatus: String){
        viewModelScope.launch {
            repository.updateTaskStatus(taskId, newStatus)
        }
    }
}

class AdminTaskViewModelFactory(private val repository: TaskRepository): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modeClass: Class<T>): T{
        if(modeClass.isAssignableFrom(AdminTaskViewModel::class.java)){
            return AdminTaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}