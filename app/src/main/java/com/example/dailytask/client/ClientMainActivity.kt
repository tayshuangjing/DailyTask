package com.example.dailytask.client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.dailytask.R
import com.example.dailytask.databinding.ActivityMainClientBinding
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskDatabase
import com.example.dailytask.db.TaskRepository

class ClientMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainClientBinding
    private lateinit var clientTaskViewModel: ClientTaskViewModel
    private lateinit var adapter: MyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_client)
        binding = ActivityMainClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "task_database").build()
        val repository = TaskRepository(database.taskDao)
        clientTaskViewModel = ViewModelProvider(this, ClientTaskViewModelFactory(repository)).get(ClientTaskViewModel::class.java)

        initRecyclerView()

        binding.btAdd.setOnClickListener {
            startActivity(Intent(this@ClientMainActivity, ClientDataActivity::class.java))
        }
    }

    private fun initRecyclerView() {
        binding.clientRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MyRecyclerViewAdapter({selectedItem: Task -> listItemClicked(selectedItem)})
        binding.clientRecyclerView.adapter = adapter
        clientTaskViewModel.getAllTasks().observe(this, Observer {
            Log.i("MyTag", it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(task: Task) {

    }
}