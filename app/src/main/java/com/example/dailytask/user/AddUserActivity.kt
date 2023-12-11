package com.example.dailytask.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
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

        spinner()

        binding.btnDelete.setOnClickListener{
            val intent = Intent(this, DeleteUserActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun displayUserList() {
        viewModel.getAllUsers().observe(this) { list ->
            Log.d("AdminMain", "List size: ${list?.size}")
        }
    }

    private fun spinner() {
        val spinner = binding.role

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
                val role = parent!!.getItemAtPosition(position).toString()

                val userIdEditText: EditText = binding.userId
                var newUserId = ""

                viewModel.getLastUserIdByRole(role).observe(this@AddUserActivity) { latestUserId ->
                    newUserId = when {
                        latestUserId != null -> {
                            val numericPart = latestUserId.substring(1).toIntOrNull() ?: 0
                            val increment = numericPart + 1
                            "${role[0]}${increment.toString().padStart(3, '0')}"
                        }
                        else -> "${role[0]}001"
                    }
                    userIdEditText.setText(newUserId)
                }

                binding.btnAddUser.setOnClickListener {
                    binding.apply {
                        if (!binding.username.text.isNullOrEmpty()) {
//                            viewModel.getLastUserIdByRole(role).observe(this@AddUserActivity) { latestUserId ->
//                                val newUserId = when {
//                                    latestUserId != null -> {
//                                        val numericPart = latestUserId.removePrefix(role[0].toString()).toIntOrNull() ?: 0
//                                        val increment = numericPart + 1
//                                        "${role[0]}${increment.toString().padStart(3, '0')}"
//                                    }
//                                    else -> "${role[0]}001"
//                                }

                                val userName = binding.username.text
                                var password = ""
                                if (role == "Client") {
                                    password = userName.toString() + "123C"
                                } else {
                                    password = userName.toString() + "123A"
                                }
                                viewModel.insert(
                                    User(
                                        userId = newUserId,
                                        userName = userName.toString(),
                                        password = password,
                                        role = role
                                    )
                                )

                                val intent = Intent(
                                    this@AddUserActivity,
                                    MainActivity::class.java
                                )
                                startActivity(intent)
                                finish()
                            }

                        else{
                            Toast.makeText(
                                this@AddUserActivity,
                                "Username is null",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected if needed
            }
        }
    }
}


