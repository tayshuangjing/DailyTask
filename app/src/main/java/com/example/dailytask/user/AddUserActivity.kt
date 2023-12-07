package com.example.dailytask.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.dailytask.MainActivity
import com.example.dailytask.R
import com.example.dailytask.databinding.ActivityAddUserBinding
import com.example.dailytask.db.AppDatabase
import com.example.dailytask.db.User
import com.example.dailytask.db.UserRepository

class AddUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddUserBinding
    private lateinit var repository: UserRepository
    private lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = UserRepository(AppDatabase.getDatabase(applicationContext).userDao())
        viewModel = ViewModelProvider(this, UserViewModelFactory(repository)).get(
            UserViewModel::class.java)

        displayUserList()

        var spinner = binding.role

        ArrayAdapter.createFromResource(
            this@AddUserActivity,
            R.array.role,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val role = parent?.getItemAtPosition(position).toString()
                binding.btnAddUser.setOnClickListener {
                    binding.apply {
                        if(binding.username.text.isNullOrEmpty()){
                           Toast.makeText(this@AddUserActivity, "Username is null",Toast.LENGTH_SHORT).show()
                        }else{
                            val userName = binding.username.text
                            var password = ""
                            if(role == "Client"){
                                password = userName.toString() + "123C"
                            }else{
                                password = userName.toString() + "123A"
                            }
                            viewModel.insert(User(userName.toString(), password, role))

                    val intent = Intent(
                        this@AddUserActivity,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                    finish()
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected if needed
            }
        }
    }

    private fun displayUserList() {
        viewModel.getAllUsers().observe(this) { list ->
            Log.d("AdminMain", "List size: ${list?.size}")
        }
    }
}


