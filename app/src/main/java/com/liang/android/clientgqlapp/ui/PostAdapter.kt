package com.liang.android.clientgqlapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liang.android.clientgqlapp.GetuserQuery
import com.liang.android.clientgqlapp.R
import kotlinx.android.synthetic.main.post_row.view.*

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val postList: MutableList<GetuserQuery.Post> = mutableListOf()

    fun setList(posts: List<GetuserQuery.Post>) {
        postList.clear()
        postList += posts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_row, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.comment.text = post.comment
    }

    inner class PostViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val comment: TextView = item.post_comment_tv
    }
}
