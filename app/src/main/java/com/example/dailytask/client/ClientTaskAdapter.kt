package com.example.dailytask.client

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytask.R
import com.example.dailytask.databinding.ListItemClientBinding
import com.example.dailytask.db.Task

class MyRecyclerViewAdapter(private val clickListener: (Task) -> Unit): RecyclerView.Adapter<MyViewHolder>() {

    private val tasksList = ArrayList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListItemClientBinding = ListItemClientBinding.inflate(layoutInflater)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(tasksList[position], clickListener)
    }

    fun setList(tasks: List<Task>){
        tasksList.clear()
        tasksList.addAll(tasks)
    }
}

class MyViewHolder(val binding: ListItemClientBinding): RecyclerView.ViewHolder(binding.root){
    val context = binding.root.context
    fun bind(task: Task, clickListener: (Task) -> Unit) {
        binding.tvTitle.text = task.title
        binding.tvDate.text = task.createDateFormat
        binding.tvName.text = task.username
        binding.tvStatus.text = task.status
        when(binding.tvStatus.text){
            "Pending" -> binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.yellow_pending))
            "Completed" -> binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.dark_green))
            "Reworked" -> binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.yellow_rework))
            "Verified" -> binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.blue_700))
        }
        binding.listItemLayout.setOnClickListener{
            clickListener(task)
        }
    }
}