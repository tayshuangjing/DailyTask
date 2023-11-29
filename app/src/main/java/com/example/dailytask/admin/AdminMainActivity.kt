package com.example.dailytask.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailytask.databinding.ActivityMainAdminBinding
import com.example.dailytask.db.Task

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var viewModel: AdminTaskViewModel
    private lateinit var adapter: AdminTaskAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, AdminTaskViewModelFactory(application)
        ).get(AdminTaskViewModel::class.java)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.adminRecyclerView.setHasFixedSize(true)
        binding.adminRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        adapter = AdminTaskAdapter { selectedId: Task -> listItemClicked(selectedId) }
        binding.adminRecyclerView.adapter = adapter
        displayTaskList()
    }

    private fun displayTaskList() {
        viewModel.getAllTasks().observe(this) { list ->
            Log.d("AdminMain", "List size: ${list?.size}")
            list?.let {
                adapter.updateList(list)
            }
        }
    }

    private fun listItemClicked(selectedId: Task) {
        val intent = Intent(this, AdminDetailActivity::class.java)
        intent.putExtra("selectedId",selectedId.id)
        startActivity(intent)
    }
}