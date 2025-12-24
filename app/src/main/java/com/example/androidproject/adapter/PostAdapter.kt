package com.example.androidproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.data.Post

class PostAdapter(
    private var posts: List<Post>,
    private val onItemClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvBody: TextView = itemView.findViewById(R.id.tvBody)
        val btnMore: Button = itemView.findViewById(R.id.btnMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.tvTitle.text = post.title
        holder.tvBody.text = post.body
        
        holder.itemView.setOnClickListener {
             onItemClick(post)
        }
        
        // Example context menu trigger could be here, or just simple click
        holder.btnMore.visibility = View.VISIBLE
        holder.btnMore.setOnClickListener {
            // Show popup menu?
            // For now, let's just trigger the same click or a separate one
            onItemClick(post)
        }
    }

    override fun getItemCount(): Int = posts.size
    
    fun updateData(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}
