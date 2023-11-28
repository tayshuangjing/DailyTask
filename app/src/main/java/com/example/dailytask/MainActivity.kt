package com.example.dailytask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dailytask.admin.AdminMainActivity
import com.example.dailytask.client.ClientMainActivity
import com.example.dailytask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdmin.setOnClickListener{
            val intent = Intent(this, AdminMainActivity::class.java)
            startActivity(intent)
        }
        binding.btnClient.setOnClickListener{
            val intent = Intent(this, ClientMainActivity::class.java)
            startActivity(intent)
        }
    }
}