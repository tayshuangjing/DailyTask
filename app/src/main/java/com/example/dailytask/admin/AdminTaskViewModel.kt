package com.example.dailytask.admin

import android.util.Log
import android.view.animation.Transformation
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskRepository
import com.example.dailytask.db.TaskWithUser
import com.example.dailytask.db.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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

    fun updateTaskStatus(taskId: Int, newStatus: String){
        viewModelScope.launch {
            repository.updateTaskStatus(taskId, newStatus)
        }
    }

    fun getTaskByUser(username: String): LiveData<List<Task>> {
        return repository.getTasksForUser(username)
    }

    fun searchTask(selectedOption: String, query:String, userId :String):LiveData<List<Task>>{

        val tasks: LiveData<List<Task>> = repository.getTasksForUser(userId)

            return tasks.map { tasks ->
                tasks.filter {
                    task ->
                    when(selectedOption){
                    "Title" -> task.title!!.contains(query,ignoreCase = true)
                    "Date" -> task.createDateFormat!!.contains(query,ignoreCase = true)
                    "Status" -> task.status!!.contains(query,ignoreCase = true)
                    else -> task.title!!.contains(query,ignoreCase = true)
                }
            }
        }
    }

    fun updateTaskCollaborators(taskId: Int, collaborators: List<String>) {
        viewModelScope.launch {
            repository.updateTaskCollaborators(taskId, collaborators)
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