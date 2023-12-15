package com.example.dailytask.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.dailytask.R
import com.example.dailytask.databinding.ActivityDetailAdminBinding
import com.example.dailytask.db.AppDatabase
import com.example.dailytask.db.TaskRepository
import com.example.dailytask.db.TaskWithUser
import com.example.dailytask.db.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AdminDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailAdminBinding
    private lateinit var taskRepository: TaskRepository
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: AdminTaskViewModel
    private lateinit var spinner: Spinner

    private var taskId: Int = 0
    private var userId: String = ""
    private var currentStatus: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskDao = AppDatabase.getDatabase(application).taskDao()
        taskRepository = TaskRepository(taskDao)
        viewModel = ViewModelProvider(this, AdminTaskViewModelFactory(taskRepository)).get(AdminTaskViewModel::class.java)


        val userDao = AppDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)


        taskId = intent.getIntExtra("taskId", 0)
        userId = intent.getStringExtra("userId").toString()
        Log.d("TaskDetail", "Received task ID:$taskId")
        Log.d("TaskDetail", "Received user ID:$userId")

        spinner()
    }

    private fun spinner(){

        spinner = binding.spinner

        lifecycleScope.launch {
            viewModel.getTaskById(taskId).collect { task ->
                Log.d("TaskDetail", "Collect block executed. Task:$task")
                if (task != null) {
                    Log.d("TaskDetail", "Task is not null. Setting values.")
                    binding.tvTitle.text = task.title
                    binding.tvContent.text = task.content
                    Log.d("username", task.userId.toString())
                    val username = userRepository.getUserByID(task.userId.toString()).firstOrNull()
                    Log.d("username", username.toString())
                    binding.tvName.text = username!!.username
                    binding.tvDate.text = task.createDateFormat
                    currentStatus = task.status.toString()

                    ArrayAdapter.createFromResource(
                        this@AdminDetailActivity,
                        R.array.task_status,
                        android.R.layout.simple_spinner_dropdown_item
                    ).also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter

                        val position = adapter.getPosition(currentStatus)
                        spinner.setSelection(position)
                    }

                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val selectedStatus = parent?.getItemAtPosition(position).toString()
                            binding.btnDone.setOnClickListener {
                                viewModel.updateTaskStatus(taskId, selectedStatus)
                                val intent = Intent(
                                    this@AdminDetailActivity,
                                    AdminMainActivity::class.java
                                )
                                intent.putExtra("userId",userId)
                                startActivity(intent)
                                finish()
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            // Handle nothing selected if needed
                        }
                    }
                } else {
                    Log.d("TaskDetail", "Task is null. Starting Error activity.")
                    val intent = Intent(this@AdminDetailActivity, Error::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}