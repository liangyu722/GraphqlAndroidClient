package com.liang.android.clientgqlapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liang.android.clientgqlapp.GetuserQuery
import com.liang.android.clientgqlapp.R
import kotlinx.android.synthetic.main.hobby_row.view.*
import kotlinx.android.synthetic.main.post_row.view.*

class HobbyAdapter : RecyclerView.Adapter<HobbyAdapter.HobbyViewHolder>() {

    private val hobbyList: MutableList<GetuserQuery.Hobby> = mutableListOf()

    fun setList(hobbies: List<GetuserQuery.Hobby>) {
        hobbyList.clear()
        hobbyList += hobbies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HobbyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hobby_row, parent, false)
        return HobbyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hobbyList.size
    }

    override fun onBindViewHolder(holder: HobbyViewHolder, position: Int) {
        val hobby = hobbyList[position]
        holder.title.text = hobby.title
        holder.title.text = hobby.description
    }

    inner class HobbyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val title: TextView = item.hobby_title_tv
        val description: TextView = item.hobby_description_tv
    }
}
