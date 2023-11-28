package com.example.dailytask.db

import android.util.Log
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks = taskDao.getAllTasks()

    suspend fun insert(task: Task) {
        Log.d("TaskRepository", "Inserting task: $task")
        taskDao.insert(task)
    }

    suspend fun update(task: Task){
        taskDao.update(task)
    }

    suspend fun delete(task: Task){
        taskDao.delete(task)
    }

    fun getTaskById(taskId: Int): Flow<Task?> {
        Log.d("TaskDao", "Querying task with ID: $taskId")
        val task = taskDao.getTaskById(taskId)
        Log.d("TaskDao", "Task result: $task")
        return task
    }
}