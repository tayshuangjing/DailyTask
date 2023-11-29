package com.example.dailytask.client

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
    fun bind(task: Task, clickListener: (Task) -> Unit) {
        binding.tvTitle.text = task.title
        binding.tvDate.text = task.createDateFormat
        binding.tvName.text = task.username
        binding.listItemLayout.setOnClickListener{
            clickListener(task)
        }
    }
}