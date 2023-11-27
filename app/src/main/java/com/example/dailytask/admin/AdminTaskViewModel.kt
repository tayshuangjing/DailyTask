package com.example.dailytask.admin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskDatabase
import com.example.dailytask.db.TaskRepository

class AdminTaskViewModel(private val application: Application): ViewModel() {

    private val repository: TaskRepository
    private val taskList = MutableLiveData<List<Task>>()
    val allTasks : LiveData<List<Task>> = taskList

    init{
        val dao = TaskDatabase.getDatabase(application).taskDao
        repository = TaskRepository(dao)

        taskList.value = listOf(
            Task(1,"Task 1", "Update data", "2023/12/20","JW", true),
            Task(2,"Task 2", "Update UI", "2023/12/21","JW", false),
            Task(3,"Task 3", "Update db", "2023/12/18","JW", true),
            Task(4,"Task 4", "Update logic", "2023/12/22","JW", false),
            Task(5,"Task 1", "Update excel", "2023/12/20","SJ", false),
            Task(6,"Task 2", "Update SRS", "2023/12/21","SJ", false)
        )
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