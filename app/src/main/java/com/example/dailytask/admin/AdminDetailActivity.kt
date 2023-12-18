package com.example.dailytask.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailytask.R
import com.example.dailytask.databinding.ActivityDetailAdminBinding
import com.example.dailytask.db.AppDatabase
import com.example.dailytask.db.TaskRepository
import com.example.dailytask.db.UserRepository
import com.example.dailytask.user.UserViewModel
import com.example.dailytask.user.UserViewModelFactory
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AdminDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailAdminBinding
    private lateinit var taskRepository: TaskRepository
    private lateinit var userRepository: UserRepository
    private lateinit var taskViewModel: AdminTaskViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var spinner: Spinner
//    private lateinit var adapter: ArrayAdapter<String>
//    private lateinit var existingNames: MutableList<String>
//    private lateinit var rvNames: MutableList<String>

    private var taskId: Int = 0
    private var userId: String = ""
    private var currentStatus: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskDao = AppDatabase.getDatabase(application).taskDao()
        taskRepository = TaskRepository(taskDao)
        taskViewModel = ViewModelProvider(this, AdminTaskViewModelFactory(taskRepository)).get(
            AdminTaskViewModel::class.java
        )


        val userDao = AppDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(userRepository)
        ).get(UserViewModel::class.java)


        taskId = intent.getIntExtra("taskId", 0)
        userId = intent.getStringExtra("userId").toString()
        Log.d("TaskDetail", "Received task ID:$taskId")
        Log.d("TaskDetail", "Received user ID:$userId")

        spinner()
    }
//
//        rvNames = mutableListOf()
//        existingNames = mutableListOf()
//        initRecyclerView()
//
//        userViewModel.getAllUsers().observe(this) { list ->
//            existingNames = list.map { it.username }.toMutableList()
//            if (binding.spinnerTeam != null) {
//                adapter = ArrayAdapter(this@AdminDetailActivity, android.R.layout.simple_spinner_dropdown_item, existingNames)
//                binding.spinnerTeam.adapter = adapter
//            }
//        }
//
//        binding.spinnerTeam.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>,
//                                        view: View, position: Int, id: Long) {
//                val selectedName = parent?.getItemAtPosition(position).toString()
//                rvNames.add(selectedName)
//                existingNames.remove(selectedName)
//                adapter = ArrayAdapter(this@AdminDetailActivity, android.R.layout.simple_spinner_dropdown_item, existingNames)
//                binding.spinnerTeam.adapter = adapter
//                rvCollaboratorAdapter.notifyDataSetChanged()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                Toast.makeText(applicationContext, "Please select a collaborator.", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    private fun initRecyclerView() {
//        rvCollaboratorAdapter = AdminColAdapter(rvNames, {selectedItem: String -> listItemClicked(selectedItem)})
//        binding.rvTeam.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        binding.rvTeam.adapter = rvCollaboratorAdapter
//    }
//
//    private fun listItemClicked(selectedItem: String) {
//        rvNames.remove(selectedItem)
//        existingNames.add(selectedItem)
//        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, existingNames)
//        binding.spinnerTeam.setAdapter(adapter)
//        rvCollaboratorAdapter.notifyDataSetChanged()
//    }

    private fun spinner(){

        spinner = binding.option

        lifecycleScope.launch {
            taskViewModel.getTaskById(taskId).collect { task ->
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
                                taskViewModel.updateTaskStatus(taskId, selectedStatus)
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