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

    @Query("DELETE FROM task_table WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Int)

    @Query("SELECT * FROM task_table ORDER BY id ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE id = :id")
    fun getTaskById(id: Int): Flow<Task?>

    @Query("UPDATE task_table SET status = :newStatus WHERE id = :id")
    suspend fun updateTaskStatus(id: Int, newStatus: String)

}