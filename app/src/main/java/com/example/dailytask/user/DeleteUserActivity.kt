package com.example.dailytask.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.dailytask.R
import com.example.dailytask.admin.AdminTaskViewHolder
import com.example.dailytask.admin.AdminTaskViewModel
import com.example.dailytask.admin.AdminTaskViewModelFactory
import com.example.dailytask.databinding.ActivityDeleteUserBinding
import com.example.dailytask.databinding.ListItemBinding
import com.example.dailytask.databinding.ListItemUserBinding
import com.example.dailytask.db.AppDatabase
import com.example.dailytask.db.Task
import com.example.dailytask.db.User
import com.example.dailytask.db.UserRepository
import kotlinx.coroutines.flow.collect


class DeleteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteUserBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: UserViewModel
    private lateinit var repository: UserRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = UserRepository(Room.databaseBuilder(applicationContext,
            AppDatabase::class.java, "task_database").build().userDao())
        viewModel = ViewModelProvider(this, UserViewModelFactory(repository)
        ).get(UserViewModel::class.java)

        initRecyclerView()
    }

    private fun initRecyclerView(){
        binding.userRecyclerView.setHasFixedSize(true)
        binding.userRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        adapter = UserAdapter { selectedUser: User-> listItemClicked(selectedUser) }
        binding.userRecyclerView.adapter = adapter
        displayUserList()
    }

    private fun displayUserList() {
        viewModel.getAllUsers().observe(this) { user ->
            adapter.updateUserList(user)
            Log.d("AdminMain", "List size: ${user?.size}")
        }
    }

    private fun listItemClicked(selectedUser: User) {
        Log.d("Clicked", "${selectedUser.username} is clicked")
        viewModel.delete(selectedUser)
    }
}

