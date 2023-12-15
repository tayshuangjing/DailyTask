package com.example.dailytask.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.dailytask.R
import com.example.dailytask.databinding.ActivityMainAdminBinding
import com.example.dailytask.db.Task
import com.example.dailytask.db.AppDatabase
import com.example.dailytask.db.TaskRepository
import com.example.dailytask.db.UserRepository
import com.example.dailytask.user.UserViewModel
import com.example.dailytask.user.UserViewModelFactory
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var taskViewModel: AdminTaskViewModel
    private lateinit var adapter: AdminTaskAdapter
    private lateinit var taskRepository: TaskRepository
    private lateinit var userViewModel: UserViewModel
    private lateinit var userRepository: UserRepository

    private var userId: String = ""
    private var currentOption: String = ""
    private var selectedOption: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskRepository = TaskRepository(Room.databaseBuilder(applicationContext,AppDatabase::class.java, "task_database").build().taskDao())
        taskViewModel = ViewModelProvider(this, AdminTaskViewModelFactory(taskRepository)
        ).get(AdminTaskViewModel::class.java)

        userRepository = UserRepository(Room.databaseBuilder(applicationContext,AppDatabase::class.java, "user_database").build().userDao())
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)
        ).get(UserViewModel::class.java)

        userId = intent.getStringExtra("userId").toString()
        Log.d("userId", userId)

        initRecyclerView(userId)

        ArrayAdapter.createFromResource(
            this,
            R.array.option,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.option.adapter = adapter

            val position = adapter.getPosition(currentOption)
            binding.option.setSelection(position)
        }

        binding.option.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedOption = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    lifecycleScope.launch {
                        taskViewModel.searchTask(selectedOption, it, userId).observe(this@AdminMainActivity){
                                filteredList -> adapter.updateList(filteredList)
                        }
                    }
                }
                return true
            }
        })
    }
    private fun initRecyclerView(username: String) {
        binding.adminRecyclerView.setHasFixedSize(true)
        binding.adminRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        adapter = AdminTaskAdapter { selectedId: Task -> listItemClicked(selectedId) }
        binding.adminRecyclerView.adapter = adapter
        displayTaskList(username)
    }

    private fun displayTaskList(username: String) {
//        taskViewModel.getAllTasks().observe(this, Observer {task ->
//            adapter.updateList(task)
//            Log.d("Task", task.toString())
//        })
        taskViewModel.getTaskByUser(username).observe(this, Observer {
            task -> adapter.updateList(task)
            Log.d("Task", task.toString())
        })
    }

    private fun listItemClicked(taskId: Task) {
        val intent = Intent(this, AdminDetailActivity::class.java)
        intent.putExtra("taskId",taskId.taskId)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }
}