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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailytask.R
import com.example.dailytask.admin.adapter.AdminColAdapter
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
    private lateinit var spinnerOption: Spinner
    private lateinit var spinnerTeam: Spinner
    private lateinit var rvNames: MutableList<String>
    private lateinit var existingNames: MutableList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var adminColAdapter: AdminColAdapter

    private var taskId: Int = 0
    private var userId: String = ""
    private var currentStatus: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskDao = AppDatabase.getDatabase(application).taskDao()
        taskRepository = TaskRepository(taskDao)
        taskViewModel = ViewModelProvider(this, AdminTaskViewModelFactory(taskRepository))
            .get(AdminTaskViewModel::class.java)

        val userDao = AppDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository))
            .get(UserViewModel::class.java)

        taskId = intent.getIntExtra("taskId", 0)
        userId = intent.getStringExtra("userId").toString()

        initRecyclerView()

        setupTeamSpinner()
        setupOptionSpinner()
    }

    private fun setupTeamSpinner() {
        val rvNames = mutableListOf<String>()

        lifecycleScope.launch {
            val task = taskViewModel.getTaskById(taskId).firstOrNull()
            rvNames.clear()
            rvNames.addAll(task?.collaborator ?: emptyList())
            Log.d("rvNames", rvNames.toString())

            existingNames = mutableListOf()
            adapter = ArrayAdapter(
                this@AdminDetailActivity,
                android.R.layout.simple_spinner_item,
                existingNames
            )

            Log.d("rvNamesOut", rvNames.toString())

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTeam.adapter = adapter

            userViewModel.getAllUsers().observe(this@AdminDetailActivity) { list ->
                existingNames.clear()
                existingNames.addAll(listOf("Select Member"))
                existingNames.addAll(
                    list.filter { user ->
                        user.userId != userId && !rvNames.contains(user.username)
                    }.mapNotNull { user ->
                        user.username?.takeIf { username -> username.isNotBlank() }
                    }
                )

                adapter.notifyDataSetChanged()
            }

            binding.spinnerTeam.onItemSelectedListener = createSpinnerItemSelectedListener()
        }
    }

    private fun setupOptionSpinner() {
        spinnerOption = binding.option

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

                    setupOptionSpinnerAdapter()
                    setupOptionSpinnerListener()
                } else {
                    Log.d("TaskDetail", "Task is null. Starting Error activity.")
                    startActivity(Intent(this@AdminDetailActivity, Error::class.java))
                    finish()
                }
            }
        }
    }

    private fun setupOptionSpinnerAdapter() {
        ArrayAdapter.createFromResource(
            this@AdminDetailActivity,
            R.array.task_status,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerOption.adapter = adapter

            val position = adapter.getPosition(currentStatus)
            spinnerOption.setSelection(position)
        }
    }

    private fun setupOptionSpinnerListener() {
        spinnerOption.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedStatus = parent?.getItemAtPosition(position).toString()
                binding.btnDone.setOnClickListener {
                    taskViewModel.updateTaskStatus(taskId, selectedStatus)
                    taskViewModel.updateTaskCollaborators(taskId, collaborators = rvNames)
                    val intent = Intent(this@AdminDetailActivity, AdminMainActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun createSpinnerItemSelectedListener() =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedName = parent?.getItemAtPosition(position).toString()
                if (selectedName != "Select Member") {
                    addNameToRecyclerView(selectedName)
                    removeNameFromSpinner(selectedName)
                    adapter.notifyDataSetChanged()
                    binding.spinnerTeam.setSelection(0)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

    private fun addNameToRecyclerView(name: String) {
        rvNames.add(name)
        adminColAdapter.notifyDataSetChanged()
    }

    private fun removeNameFromSpinner(name: String) {
        existingNames.remove(name)
        adapter.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        lifecycleScope.launch {
            val task = taskViewModel.getTaskById(taskId).firstOrNull()

            rvNames = task?.collaborator?.toMutableList() ?: mutableListOf()

            adminColAdapter = AdminColAdapter(rvNames) { selectedItem: String ->
                listItemClicked(selectedItem)
            }

            binding.rvTeam.layoutManager =
                LinearLayoutManager(this@AdminDetailActivity, LinearLayoutManager.VERTICAL, false)
            binding.rvTeam.adapter = adminColAdapter
        }
    }

    private fun listItemClicked(selectedItem: String) {
        if (!rvNames.contains(selectedItem)) {
            addNameToRecyclerView(selectedItem)
            removeNameFromSpinner(selectedItem)
        } else {
            deleteItemFromRecyclerView(selectedItem)
        }
    }

    private fun deleteItemFromRecyclerView(name: String) {
        rvNames.remove(name)
        adminColAdapter.notifyDataSetChanged()
        existingNames.add(name)
        adapter.notifyDataSetChanged()
    }
}


