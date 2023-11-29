package com.example.dailytask.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.dailytask.databinding.ActivitySecondBinding
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskDatabase
import com.example.dailytask.db.TaskRepository

class ClientDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var clientTaskViewModel: ClientTaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "task_database").build()
        val repository = TaskRepository(database.taskDao)
        clientTaskViewModel = ViewModelProvider(this, ClientTaskViewModelFactory(repository)).get(ClientTaskViewModel::class.java)

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
            clientTaskViewModel.insert(Task(null, userInputTitle, userInputContent, userInputDate, userInputName, false))
            etTitle.text.clear()
            etContent.text.clear()
            etDate.text.clear()
            etName.text.clear()
        }
    }
}