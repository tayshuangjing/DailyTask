package com.example.dailytask.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.dailytask.R
import com.example.dailytask.databinding.ActivityDetailAdminBinding
import com.example.dailytask.db.AppDatabase
import com.example.dailytask.db.TaskRepository
import kotlinx.coroutines.launch

class AdminDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailAdminBinding
    private lateinit var repository: TaskRepository
    private lateinit var viewModel: AdminTaskViewModel
    private lateinit var spinner: Spinner
    private var selectedId: Int = 0
    private var currentStatus: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = AppDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(dao)
        viewModel = ViewModelProvider(this, AdminTaskViewModelFactory(repository)).get(AdminTaskViewModel::class.java)

        selectedId = intent.getIntExtra("selectedId", 0)
        Log.d("TaskDetail", "Received ID:$selectedId")

        spinner()
    }

    private fun spinner(){

        spinner = binding.spinner

        lifecycleScope.launch {
            viewModel.getTaskById(selectedId).collect { task ->
                Log.d("TaskDetail", "Collect block executed. Task:$task")
                if (task != null) {
                    Log.d("TaskDetail", "Task is not null. Setting values.")
                    binding.tvTitle.text = task.title
                    binding.tvContent.text = task.content
//                    binding.tvName.text = task.username
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
                                viewModel.updateTaskStatus(selectedId, selectedStatus)
                                val intent = Intent(
                                    this@AdminDetailActivity,
                                    AdminMainActivity::class.java
                                )
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