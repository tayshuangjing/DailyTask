package com.example.dailytask.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.dailytask.databinding.ActivityMainAdminBinding
import com.example.dailytask.db.Task
import com.example.dailytask.db.AppDatabase
import com.example.dailytask.db.TaskRepository
import com.example.dailytask.db.UserRepository
import com.example.dailytask.user.UserViewModel
import kotlinx.coroutines.launch

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var taskViewModel: AdminTaskViewModel
    private lateinit var adapter: AdminTaskAdapter
    private lateinit var taskRepository: TaskRepository
    private lateinit var userViewModel: UserViewModel
    private lateinit var userRepository: UserRepository

    private var username: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskRepository = TaskRepository(Room.databaseBuilder(applicationContext,AppDatabase::class.java, "task_database").build().taskDao())
        taskViewModel = ViewModelProvider(this, AdminTaskViewModelFactory(taskRepository)
        ).get(AdminTaskViewModel::class.java)

        initRecyclerView()

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    lifecycleScope.launch {
                        taskViewModel.searchTask(it).observe(this@AdminMainActivity){
                                filteredList -> adapter.updateList(filteredList)
                        }
                    }
                }
                return true
            }
        })
    }
    private fun initRecyclerView() {
        binding.adminRecyclerView.setHasFixedSize(true)
        binding.adminRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        adapter = AdminTaskAdapter { selectedId: Task -> listItemClicked(selectedId) }
        binding.adminRecyclerView.adapter = adapter
        displayTaskList()
    }

    private fun displayTaskList() {
        taskViewModel.getAllTasks().observe(this, Observer {task ->
            adapter.updateList(task)
            Log.d("Task", task.toString())
        })
    }

    private fun listItemClicked(selectedId: Task) {
        val intent = Intent(this, AdminDetailActivity::class.java)
        intent.putExtra("selectedId",selectedId.taskId)
        startActivity(intent)
        finish()
    }
}