package com.example.dailytask.client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.dailytask.databinding.ActivityAddClientBinding
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskDatabase
import com.example.dailytask.db.TaskRepository
import java.time.LocalDateTime

class ClientAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddClientBinding
    private lateinit var clientTaskViewModel: ClientTaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "task_database").build()
        val repository = TaskRepository(database.taskDao)
        clientTaskViewModel = ViewModelProvider(this, ClientTaskViewModelFactory(repository)).get(ClientTaskViewModel::class.java)

        binding.btSave.setOnClickListener {
            save()
        }

        binding.btCancel.setOnClickListener {
            cancel()
        }
    }

    private fun save() {
        Log.i("mytag", "save button is clicked")
        binding.apply {
            if (!etTitle.text.isEmpty() && !etContent.text.isEmpty() && !etName.text.isEmpty()){
                val userInputTitle = etTitle.text.toString()
                val userInputContent = etContent.text.toString()
                val userInputName = etName.text.toString()
                clientTaskViewModel.insert(Task(null, userInputTitle, userInputContent, LocalDateTime.now(), userInputName, false))
                etTitle.text.clear()
                etContent.text.clear()
                etName.text.clear()
                val intent = Intent(this@ClientAddActivity, ClientMainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Please fill in the empty field.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun cancel() {
        finish()
    }
}