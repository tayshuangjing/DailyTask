package com.example.dailytask.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.dailytask.R
import com.example.dailytask.databinding.ActivityMainClientBinding
import com.example.dailytask.databinding.ActivitySecondBinding
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskDatabase
import com.example.dailytask.db.TaskRepository

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var taskViewModel: TaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "task_database").build()
        val repository = TaskRepository(database.taskDao)
        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(repository)).get(TaskViewModel::class.java)

        binding.btSave.setOnClickListener {
            save()
        }
    }

    private fun save() {
        binding.apply {
            val userInputTitle = etTitle.text.toString()
            val userInputContent = etContent.text.toString()
            val userInputDate = etDate.text.toString()
            val userInputName = etName.text.toString()
            taskViewModel.insert(Task(null, userInputTitle, userInputContent, userInputDate, userInputName, false))
            etTitle.text.clear()
            etContent.text.clear()
            etDate.text.clear()
            etName.text.clear()
        }
    }
}