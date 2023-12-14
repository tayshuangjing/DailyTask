package com.example.dailytask.user

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytask.databinding.ListItemUserBinding
import com.example.dailytask.db.User

class UserAdapter(private val clickListener: (User) -> Unit) :
    RecyclerView.Adapter<UserViewHolder>() {
    private val userList = ArrayList<User>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemUserBinding = ListItemUserBinding.inflate(layoutInflater)
        return UserViewHolder(binding,clickListener)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    fun updateUserList(newList: List<User>){
        userList.clear()
        userList.addAll(newList)
        Log.d("User", newList.toString())
        notifyDataSetChanged()
    }
}

class UserViewHolder(
    private val binding: ListItemUserBinding,
    private val clickListener: (User) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(user: User){
        binding.tvUser.text = user.username
        binding.imageButton.setOnClickListener{
            clickListener(user)
        }
    }
}