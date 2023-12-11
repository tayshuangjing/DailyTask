package com.example.dailytask.client

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.dailytask.R
import com.example.dailytask.databinding.ActivityUpdateClientBinding
import com.example.dailytask.db.AppDatabase
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskRepository
import com.example.dailytask.db.UserRepository
import com.example.dailytask.user.UserViewModel
import com.example.dailytask.user.UserViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class ClientUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateClientBinding
    private lateinit var clientTaskViewModel: ClientTaskViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var selectedStatus: String
    private lateinit var rvCollaboratorAdapter: ClientColAdapter
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var rvNames: MutableList<String>
    private lateinit var existingNames: MutableList<String>
    private val calendar = Calendar.getInstance()
    private var date = LocalDateTime.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "task_database").build()
        val taskRepository = TaskRepository(database.taskDao())
        clientTaskViewModel = ViewModelProvider(this, ClientTaskViewModelFactory(taskRepository)).get(ClientTaskViewModel::class.java)

        val userRepository = UserRepository(database.userDao())
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        val taskId = intent.getIntExtra("task_id", 0)
        rvNames = mutableListOf()
        existingNames = mutableListOf()

        //init data
        lifecycleScope.launch {
            clientTaskViewModel.getTaskById(taskId).collect { task ->
                if (task != null) {
                    binding.apply {
                        etTitle.setText(task.title)
                        etContent.setText(task.content)
                        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
                        val formattedDate = formatter.format(task.date)
                        etDate.setText(formattedDate)
                        rvNames = (task.collaborator ?: emptyList()).toMutableList()
                        userViewModel.getAllUsers().observe(this@ClientUpdateActivity) { list ->
                            existingNames = list.map { it.userName }.toMutableList()
                            existingNames.removeAll(rvNames)
                            adapter = ArrayAdapter(this@ClientUpdateActivity, android.R.layout.simple_dropdown_item_1line, existingNames)
                            binding.etCol.setAdapter(adapter)
                        }

                        initRecyclerView()
                    }
                }
            }
        }

        //init calendar view
        binding.etDate.inputType = InputType.TYPE_NULL
        binding.etDate.setOnClickListener {
            showCalendarPicker()
        }

        //init autocomplete text view
        binding.etCol.setOnItemClickListener { _, _, position, _ ->
            val selectedName = adapter.getItem(position).toString()
            rvNames.add(selectedName)
            existingNames.remove(selectedName)
            adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, existingNames)
            binding.etCol.setAdapter(adapter)
            rvCollaboratorAdapter.notifyDataSetChanged()
        }

        //init spinner
        val status = resources.getStringArray(R.array.task_status)
        if (binding.spStatus != null){
            val adapter = ArrayAdapter(this@ClientUpdateActivity, android.R.layout.simple_spinner_dropdown_item, status)
            binding.spStatus.adapter = adapter
        }

        binding.spStatus.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    selectedStatus = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    Toast.makeText(applicationContext, "Please select a status.", Toast.LENGTH_LONG).show()
                }
            }


        binding.btUpdate.setOnClickListener {
            update(taskId, selectedStatus)
        }

        binding.btnDelete.setOnClickListener {
            delete(taskId)
        }
    }

    private fun showCalendarPicker() {
        val dialogPickerDialog = DatePickerDialog(this, { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = formatter.format(selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())

            date = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            binding.etDate.setText(formattedDate)
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialogPickerDialog.show()
    }

    private fun initRecyclerView() {
        rvCollaboratorAdapter = ClientColAdapter(rvNames, {selectedItem: String -> listItemClicked(selectedItem)})
        binding.rvCollaborator.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCollaborator.adapter = rvCollaboratorAdapter
    }

    private fun update(taskId: Int, selectedStatus: String) {
        binding.apply {
            if (!etTitle.text.isEmpty() && !etContent.text.isEmpty() && !rvNames.isEmpty()){
                val userInputTitle = etTitle.text.toString()
                val userInputContent = etContent.text.toString()
//                val userInputName = etName.text.toString()
                val userInputDate = date
                clientTaskViewModel.update(Task(taskId, userInputTitle, userInputContent, userInputDate, null, rvNames, selectedStatus))
                etTitle.text.clear()
                etContent.text.clear()
//                etName.text.clear()
                val intent = Intent(this@ClientUpdateActivity, ClientMainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Please fill in the empty field.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun delete(taskId: Int) {
        clientTaskViewModel.deleteTaskById(taskId)
        val intent = Intent(this@ClientUpdateActivity, ClientMainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun listItemClicked(selectedItem: String) {
        rvNames.remove(selectedItem)
        existingNames.add(selectedItem)
        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, existingNames)
        binding.etCol.setAdapter(adapter)
        rvCollaboratorAdapter.notifyDataSetChanged()
    }
}