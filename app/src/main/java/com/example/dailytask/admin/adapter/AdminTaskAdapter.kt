package com.example.dailytask.admin.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytask.R
import com.example.dailytask.databinding.ListItemBinding
import com.example.dailytask.db.Task


private val taskList = ArrayList<Task>()
class AdminTaskAdapter(private val clickListener: (Task) -> Unit) :
    RecyclerView.Adapter<AdminTaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminTaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding = ListItemBinding.inflate(layoutInflater)
        return AdminTaskViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: AdminTaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
    }

    fun updateList(newList: List<Task>) {
        taskList.clear()
        taskList.addAll(newList)
        notifyDataSetChanged()
    }

}

class AdminTaskViewHolder(
    private val binding: ListItemBinding,
    private val clickListener: (Task) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {

        updateColor()
        binding.cardView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Handle touch down
                    binding.cardView.setCardBackgroundColor(
                        ContextCompat.getColor(binding.root.context, R.color.blue_200)
                    )
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // Handle touch release or cancel
                    binding.cardView.setCardBackgroundColor(
                        ContextCompat.getColor(binding.root.context, android.R.color.white)
                    )
                    clickListener(taskList[adapterPosition])
                }
            }
            true
        }
    }

    private fun updateColor(){
        val context = binding.root.context

        when (binding.tvComplete.text) {
            "Pending" -> {
                binding.tvComplete.setTextColor(ContextCompat.getColor(context, R.color.yellow_pending))
            }
            "Completed" -> {
                binding.tvComplete.setTextColor(ContextCompat.getColor(context, R.color.dark_green))
            }
            "Reworked" -> {
                binding.tvComplete.setTextColor(ContextCompat.getColor(context, R.color.yellow_rework))
            }
            "Verified" -> {
                binding.tvComplete.setTextColor(ContextCompat.getColor(context, R.color.blue_700))
            }
            else -> {
                binding.tvComplete.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }

    fun bind(task: Task) {
        binding.tvTitle.text = task.title
        binding.tvDate.text = task.createDateFormat
//        binding.tvName.text = task.username
        binding.tvComplete.text = task.status
        updateColor()
    }
}