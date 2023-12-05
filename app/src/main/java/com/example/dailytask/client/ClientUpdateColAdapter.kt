package com.example.dailytask.client

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytask.databinding.ListItemCollaboratorBinding

class ClientUpdateColAdapter(private val usersList: List<String>, private val clickListener: (String) -> Unit): RecyclerView.Adapter<UpdateColViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateColViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListItemCollaboratorBinding = ListItemCollaboratorBinding.inflate(layoutInflater)
        return UpdateColViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: UpdateColViewHolder, position: Int) {
//        val user = usersList[position]
//        holder.myTextView.text = user
        holder.bind(usersList[position], clickListener)
    }
}

class UpdateColViewHolder(val binding: ListItemCollaboratorBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(user: String, clickListener: (String) -> Unit) {
        binding.tvCollaborator.text = user
        binding.btDelete.setOnClickListener {
            clickListener(user)
        }
    }
}