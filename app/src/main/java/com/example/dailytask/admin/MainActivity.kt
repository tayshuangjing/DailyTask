package com.example.dailytask.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputBinding
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailytask.R
import com.example.dailytask.databinding.ActivityMainAdminBinding
import com.example.dailytask.db.Task

class MainActivity : AppCompatActivity() {
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

        viewModel.allTasks.observe(this){
            list -> list?.let{
                adapter.updateList(list)
        }
        }
    }

    private fun initRecyclerView() {
        binding.adminRecyclerView.setHasFixedSize(true)
        binding.adminRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        adapter = AdminTaskAdapter({selectedItem: Task -> listItemClicked(selectedItem)})
        binding.adminRecyclerView.adapter = adapter
    }

    private fun listItemClicked(selectedItem: Task) {
        val intent = Intent(this, AdminDetailActivity::class.java)
        intent.putExtra("selectedItem",selectedItem.id)
        startActivity(intent)
    }
}