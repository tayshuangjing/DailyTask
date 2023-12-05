package com.example.dailytask.client

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.dailytask.databinding.ActivityUpdateClientBinding
import com.example.dailytask.db.Task
import com.example.dailytask.db.AppDatabase
import com.example.dailytask.db.TaskRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ClientUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateClientBinding
    private lateinit var clientTaskViewModel: ClientTaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "task_database").build()
        val repository = TaskRepository(database.taskDao())
        clientTaskViewModel = ViewModelProvider(this, ClientTaskViewModelFactory(repository)).get(ClientTaskViewModel::class.java)

        val taskId = intent.getIntExtra("task_id", 0)

        lifecycleScope.launch {
            clientTaskViewModel.getTaskById(taskId).collect { task ->
                binding.apply {
                    etTitle.setText(task?.title)
                    etContent.setText(task?.content)
                    etName.setText(task?.userId)
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
            if (!etTitle.text.isEmpty() && !etContent.text.isEmpty() && !etName.text.isEmpty()){
                val userInputTitle = etTitle.text.toString()
                val userInputContent = etContent.text.toString()
                val userInputName = etName.text.toString()
                val userInputDate = LocalDateTime.now()
                val status = "pending"
                clientTaskViewModel.update(Task(taskId, userInputTitle, userInputContent, userInputDate, userInputName, status))
                etTitle.text.clear()
                etContent.text.clear()
                etName.text.clear()
                val intent = Intent(this@ClientUpdateActivity, ClientMainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Please fill in the empty field.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun delete(taskId: Int) {
        clientTaskViewModel.deleteTaskById(taskId)
        val intent = Intent(this@ClientUpdateActivity, ClientMainActivity::class.java)
        startActivity(intent)
        finish()
    }
}