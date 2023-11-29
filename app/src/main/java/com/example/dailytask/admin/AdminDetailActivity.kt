package com.example.dailytask.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.dailytask.R
import com.example.dailytask.databinding.ActivityDetailAdminBinding
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskDatabase
import com.example.dailytask.db.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class AdminDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailAdminBinding
    private lateinit var repository: TaskRepository
    private lateinit var viewModel: AdminTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = TaskDatabase.getDatabase(application).taskDao
        repository = TaskRepository(dao)
        viewModel = ViewModelProvider(this, AdminTaskViewModelFactory(application)
        ).get(AdminTaskViewModel::class.java)

        val id = intent.getIntExtra("selectedId", 0)
        Log.d("TaskDetail", "Received ID: $id")

//        var status = arrayOf("Completed", "Reworked", "Verified")
//        val statusArray = ArrayAdapter(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, status)
//        statusArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//        with(binding.spinner){
//            adapter = statusArray
//            setSelection(0,false)
//            onItemSelectedListener = this
//            prompt= "Mark Status"
//            gravity = Gravity.CENTER
//        }

        lifecycleScope.launch {
            viewModel.getTaskById(id).collect { task ->
                Log.d("TaskDetail", "Collect block executed. Task: $task")
                if (task != null) {
                    Log.d("TaskDetail", "Task is not null. Setting values.")
                    binding.tvTitle.text = task.title
                    binding.tvContent.text = task.content
                    binding.tvName.text = task.username
                    binding.tvDate.text = task.createDateFormat
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