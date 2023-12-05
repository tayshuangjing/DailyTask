package com.example.dailytask.client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.dailytask.databinding.ActivityAddClientBinding
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskDatabase
import com.example.dailytask.db.TaskRepository
import java.time.LocalDateTime

class ClientAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddClientBinding
    private lateinit var clientTaskViewModel: ClientTaskViewModel
    private lateinit var rvCollaboratorAdapter: ClientColAdapter
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var existingNames: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "task_database").build()
        val repository = TaskRepository(database.taskDao)
        clientTaskViewModel = ViewModelProvider(this, ClientTaskViewModelFactory(repository)).get(ClientTaskViewModel::class.java)

        //init recycler view
//        rvNames = mutableListOf()
        initRecyclerView()

        //init autocomplete text view
        existingNames = clientTaskViewModel.existingNames
        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, existingNames)
        binding.etCol.setAdapter(adapter)
        binding.etCol.setOnItemClickListener { _, _, position, _ ->
            val selectedName = adapter.getItem(position).toString()
            clientTaskViewModel.rvNames.add(selectedName)
            existingNames.remove(selectedName)
            adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, existingNames)
            binding.etCol.setAdapter(adapter)
            rvCollaboratorAdapter.notifyDataSetChanged()
        }

        binding.btSave.setOnClickListener {
            save()
        }

        binding.btCancel.setOnClickListener {
            cancel()
        }
    }

    private fun initRecyclerView() {
        rvCollaboratorAdapter = ClientColAdapter(clientTaskViewModel.rvNames, {selectedItem: String -> listItemClicked(selectedItem)})
        binding.rvCollaborator.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCollaborator.adapter = rvCollaboratorAdapter
    }

    private fun save() {
        binding.apply {
            if (!etTitle.text.isEmpty() && !etContent.text.isEmpty() && !etName.text.isEmpty() && !clientTaskViewModel.rvNames.isEmpty()){
                val userInputTitle = etTitle.text.toString()
                val userInputContent = etContent.text.toString()
                val userInputName = etName.text.toString()
                val status = "Pending"
                clientTaskViewModel.insert(Task(null, userInputTitle, userInputContent, LocalDateTime.now(), userInputName, status))
                etTitle.text.clear()
                etContent.text.clear()
                etName.text.clear()
                val intent = Intent(this@ClientAddActivity, ClientMainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Please fill in the empty field.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun cancel() {
        finish()
    }

    private fun listItemClicked(selectedItem: String) {
        clientTaskViewModel.rvNames.remove(selectedItem)
        existingNames.add(selectedItem)
        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, existingNames)
        binding.etCol.setAdapter(adapter)
        rvCollaboratorAdapter.notifyDataSetChanged()
    }
}