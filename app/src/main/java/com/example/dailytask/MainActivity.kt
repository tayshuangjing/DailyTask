package com.example.dailytask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.dailytask.admin.AdminMainActivity
import com.example.dailytask.admin.AdminTaskViewModel
import com.example.dailytask.admin.AdminTaskViewModelFactory
import com.example.dailytask.client.ClientMainActivity
import com.example.dailytask.databinding.ActivityMainBinding
import com.example.dailytask.db.AppDatabase
import com.example.dailytask.db.TaskRepository
import com.example.dailytask.db.User
import com.example.dailytask.db.UserRepository
import com.example.dailytask.user.AddUserActivity
import com.example.dailytask.user.UserViewModel
import com.example.dailytask.user.UserViewModelFactory
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var repository: UserRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adminIntent = Intent(this, AdminMainActivity::class.java)
        val clientIntent = Intent(this, ClientMainActivity::class.java)

        repository = UserRepository(
            Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "task_database").build().userDao())
        userViewModel = ViewModelProvider(this, UserViewModelFactory(repository)
        ).get(UserViewModel::class.java)

        binding.btnSubmit.setOnClickListener{
            if(binding.username.text.isNullOrEmpty() || binding.password.text.isNullOrEmpty()){
                Toast.makeText(this,"Username or Password should not be empty",Toast.LENGTH_SHORT).show()
            }else{
                val name = binding.username.text.toString()
                val password = binding.password.text.toString()
                Log.d("User", "$name $password")
                lifecycleScope.launch {
                    val matchName = userViewModel.getUserByID(name).firstOrNull()
                    Log.d("Match user", matchName.toString())
                    if(matchName!=null){
                        if(matchName.password == password){
                            binding.username.text.clear()
                            binding.password.text.clear()
                            when(matchName.role){
                                "Admin" -> startActivity(adminIntent)
                                "Client" -> startActivity(clientIntent)
                                else -> Toast.makeText(this@MainActivity,"Invalid Role", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this@MainActivity,"Password not matched", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this@MainActivity,"User not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnAddUser.setOnClickListener{
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }
    }
}