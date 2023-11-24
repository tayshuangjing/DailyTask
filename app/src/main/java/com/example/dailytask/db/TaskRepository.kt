package com.example.dailytask.db

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks = taskDao.getAllTasks()

    suspend fun insert(task: Task){
        taskDao.insert(task)
    }

    suspend fun update(task: Task){
        taskDao.update(task)
    }

    suspend fun delete(task: Task){
        taskDao.delete(task)
    }
}