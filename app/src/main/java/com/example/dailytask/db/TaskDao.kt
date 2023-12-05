package com.example.dailytask.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task : Task)

    @Update
    suspend fun update(task: Task)

    @Query("DELETE FROM task_table WHERE taskId = :taskId")
    suspend fun deleteTaskById(taskId: Int)

    @Query("SELECT * FROM task_table ORDER BY taskId ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE taskId = :taskId")
    fun getTaskById(taskId: Int): Flow<Task?>

    @Query("UPDATE task_table SET status = :newStatus WHERE taskId = :taskId")
    suspend fun updateTaskStatus(taskId: Int, newStatus: String)
}