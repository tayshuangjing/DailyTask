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
import com.example.dailytask.db.AppDatabase
import com.example.dailytask.db.TaskRepository

class ClientMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainClientBinding
    private lateinit var clientTaskViewModel: ClientTaskViewModel
    private lateinit var adapter: MyRecyclerViewAdapter
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_client)
        binding = ActivityMainClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "task_database").build()
        val repository = TaskRepository(database.taskDao())
        clientTaskViewModel = ViewModelProvider(this, ClientTaskViewModelFactory(repository)).get(ClientTaskViewModel::class.java)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        initRecyclerView()

        userId = intent.getStringExtra("userId").toString()
        Log.d("useridmain", userId)

        binding.btAdd.setOnClickListener {
            val clientIntent = Intent(this@ClientMainActivity, ClientAddActivity::class.java)
            clientIntent.putExtra("userId",userId)
            startActivity(clientIntent)
        }
    }

    private fun initRecyclerView() {
        binding.clientRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MyRecyclerViewAdapter({selectedItem: Task -> listItemClicked(selectedItem)})
        binding.clientRecyclerView.adapter = adapter
        displayTask()
    }

    private fun displayTask() {
//        clientTaskViewModel.getAllTasks().observe(this, Observer {
//            adapter.setList(it)
//            adapter.notifyDataSetChanged()
//        })
        val userId = sharedPreferencesHelper.userId
        Log.d("mytag", "user id in client: $userId")
//        val username = "qwe"
        if (userId != null) {
            clientTaskViewModel.getTasksByUserId(userId).observe(this, Observer {
                adapter.setList(it)
                adapter.notifyDataSetChanged()
            })
        }
    }

    private fun listItemClicked(task: Task) {
        val intent = Intent(this@ClientMainActivity, ClientUpdateActivity::class.java)
        intent.putExtra("task_id", task.taskId)
        startActivity(intent)
    }
}
