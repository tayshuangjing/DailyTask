package com.example.dailytask.admin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskDatabase
import com.example.dailytask.db.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

class AdminTaskViewModel(private val application: Application): ViewModel() {

    val dao = TaskDatabase.getDatabase(application).taskDao
    private var repository: TaskRepository = TaskRepository(dao)
    val tasks = repository.allTasks

    fun getTaskById(taskId: Int): Flow<Task?> {
        return repository.getTaskById(taskId)
    }

    fun getAllTasks() = liveData {
        tasks.collect{
            emit(it)
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
}

class AdminTaskViewModelFactory(private val application: Application): ViewModelProvider.AndroidViewModelFactory(application){
    override fun <T: ViewModel> create(modeClass: Class<T>): T{
        if(modeClass.isAssignableFrom(AdminTaskViewModel::class.java)){
            return AdminTaskViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}