package com.example.dailytask.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytask.databinding.ListItemCollaboratorBinding

class AdminColAdapter(private val userList: List<String>, private val clickListener: (String) -> Unit)
    : RecyclerView.Adapter<CollaboratorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollaboratorViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListItemCollaboratorBinding = ListItemCollaboratorBinding.inflate(layoutInflater)
        return CollaboratorViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: CollaboratorViewHolder, position: Int) {
        holder.bind(userList[position], clickListener)
    }
}

class CollaboratorViewHolder(val binding: ListItemCollaboratorBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(user: String, clickListener: (String) -> Unit) {
        binding.tvCollaborator.text = user
        binding.btnDelete.setOnClickListener {
            clickListener(user)
        }
    }
}