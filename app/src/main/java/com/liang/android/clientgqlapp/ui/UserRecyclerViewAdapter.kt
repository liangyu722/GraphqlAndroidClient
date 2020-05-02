package com.liang.android.clientgqlapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liang.android.clientgqlapp.R
import com.liang.android.clientgqlapp.UsersQuery
import kotlinx.android.synthetic.main.user_row.view.*

class UserRecyclerViewAdapter(val block: (UsersQuery.User) -> Unit)
    : RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder>() {

    private val userList: MutableList<UsersQuery.User> = mutableListOf()

    fun setUsers(users: List<UsersQuery.User>) {
        userList.clear()
        userList += users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_row, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.nameTextView.text = user.name
        holder.ageTextView.text = user.age.toString()
        holder.professionTextView.text = user.profession
    }

    inner class UserViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val nameTextView: TextView = item.tv_user_id
        val ageTextView: TextView = item.tv_age_id
        val professionTextView: TextView = item.tv_profession_id

        init {
            item.setOnClickListener {
                block.invoke(userList[adapterPosition])
            }
        }
    }
}
