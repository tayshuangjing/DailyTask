package com.example.dailytask.client

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.dailytask.databinding.ActivityAddClientBinding
import com.example.dailytask.db.AppDatabase
import com.example.dailytask.db.Task
import com.example.dailytask.db.TaskRepository
import com.example.dailytask.db.UserRepository
import com.example.dailytask.user.UserViewModel
import com.example.dailytask.user.UserViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class ClientAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddClientBinding
    private lateinit var clientTaskViewModel: ClientTaskViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var rvCollaboratorAdapter: ClientColAdapter
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var existingNames: MutableList<String>
    private lateinit var rvNames: MutableList<String>
    private val calendar = Calendar.getInstance()
    private var date = LocalDateTime.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "task_database").build()
        val taskRepository = TaskRepository(database.taskDao())
        clientTaskViewModel = ViewModelProvider(this, ClientTaskViewModelFactory(taskRepository)).get(ClientTaskViewModel::class.java)

        val userRepository = UserRepository(database.userDao())
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        //init recycler view
        rvNames = mutableListOf()
        existingNames = mutableListOf()
        initRecyclerView()

        //init calendar view
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = formatter.format(date)
        binding.etDate.inputType = InputType.TYPE_NULL
        binding.etDate.setText(formattedDate)
        binding.etDate.setOnClickListener {
            showCalendarPicker()
        }

        //init autocomplete text view
        //        existingNames = clientTaskViewModel.existingNames
        userViewModel.getAllUsers().observe(this) { list ->
            existingNames = list.map { it.userName }.toMutableList()
            adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, existingNames)
            binding.etCol.setAdapter(adapter)
        }
        binding.etCol.setOnItemClickListener { _, _, position, _ ->
            val selectedName = adapter.getItem(position).toString()
            rvNames.add(selectedName)
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

    private fun showCalendarPicker() {
        val dialogPickerDialog = DatePickerDialog(this, { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)
//            val dateFormat = SimpleDateFormat("dd/MM/YYYY", Locale.getDefault())
//            val formattedDate = dateFormat.format(selectedDate.time)

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

    private fun save() {
        binding.apply {
            if (!etTitle.text.isEmpty() && !etContent.text.isEmpty() && !rvNames.isEmpty()){
                val userInputTitle = etTitle.text.toString()
                val userInputContent = etContent.text.toString()
//                val userInputName = etName.text.toString()
                val userInputDate = date
                val status = "Pending"
                clientTaskViewModel.insert(Task(null, userInputTitle, userInputContent, userInputDate, null, rvNames, status))
                etTitle.text.clear()
                etContent.text.clear()
//                etName.text.clear()
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
        rvNames.remove(selectedItem)
        existingNames.add(selectedItem)
        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, existingNames)
        binding.etCol.setAdapter(adapter)
        rvCollaboratorAdapter.notifyDataSetChanged()
    }
}