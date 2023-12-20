package com.example.dailytask.db

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks = taskDao.getAllTasks()

    suspend fun insert(task: Task) {
        Log.d("TaskRepository", "Inserting task: $task")
        taskDao.insert(task)
    }

    suspend fun update(task: Task){
        taskDao.update(task)
    }

    suspend fun deleteTaskById(taskId: Int){
        taskDao.deleteTaskById(taskId)
    }

    suspend fun updateTaskStatus(taskId: Int, newStatus: String){
        taskDao.updateTaskStatus(taskId,newStatus)
    }

    fun getTasksForUser(userId: String): LiveData<List<Task>> {
        Log.d("DAO", userId)
        return taskDao.getTasksForUser(userId)
    }

    fun getTaskById(taskId: Int): Flow<Task?> {
        Log.d("TaskDao", "Querying task with ID: $taskId")
        return taskDao.getTaskById(taskId).onEach { task: Task? ->
        Log.d("TaskDao", "Task result: $task")
        }
    }

    fun getTasksByUserId(userId: String): Flow<List<Task>> {
        return taskDao.getTasksByUserId(userId)
    }
}