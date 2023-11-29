package com.example.dailytask.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.dailytask.admin.Error
import com.example.dailytask.databinding.ActivitySecondBinding
import com.example.dailytask.databinding.ActivityUpdateBinding
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskDatabase
import com.example.dailytask.db.TaskRepository
import kotlinx.coroutines.launch

class ClientUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var clientTaskViewModel: ClientTaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "task_database").build()
        val repository = TaskRepository(database.taskDao)
        clientTaskViewModel = ViewModelProvider(this, ClientTaskViewModelFactory(repository)).get(ClientTaskViewModel::class.java)

        val taskId = intent.getIntExtra("task_id", 0)

        lifecycleScope.launch {
            clientTaskViewModel.getTaskById(taskId).collect { task ->
                binding.apply {
                    etTitle.setText(task?.title)
                    etContent.setText(task?.content)
                    etDate.setText(task?.date)
                    etName.setText(task?.username)
                }
            }
        }

        binding.btUpdate.setOnClickListener {
            update(taskId)
        }

        binding.btnDelete.setOnClickListener {
            delete(taskId)
        }
    }

    private fun update(taskId: Int) {
        binding.apply {
            if (!etTitle.text.isEmpty() && !etContent.text.isEmpty() && !etDate.text.isEmpty() && !etName.text.isEmpty()){
                val userInputTitle = etTitle.text.toString()
                val userInputContent = etContent.text.toString()
                val userInputDate = etDate.text.toString()
                val userInputName = etName.text.toString()
                clientTaskViewModel.update(Task(taskId, userInputTitle, userInputContent, userInputDate, userInputName, false))
                etTitle.text.clear()
                etContent.text.clear()
                etDate.text.clear()
                etName.text.clear()
                finish()
            } else {
                Toast.makeText(applicationContext, "Please fill in the empty field.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun delete(taskId: Int) {
        clientTaskViewModel.deleteTaskById(taskId)
        finish()
    }
}