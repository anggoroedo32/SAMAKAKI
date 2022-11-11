package com.awp.samakaki.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.R
import com.awp.samakaki.databinding.LayoutPostContentBinding
import com.awp.samakaki.response.DataItem
import com.awp.samakaki.response.DataPosts
import com.awp.samakaki.response.ItemPosts
import com.bumptech.glide.Glide

class UserPostsAdapter(private var list: List<ItemPosts>) : RecyclerView.Adapter<UserPostsAdapter.UserPostsViewHolder>() {

    inner class UserPostsViewHolder (val binding: LayoutPostContentBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostsViewHolder {
        val binding = LayoutPostContentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserPostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserPostsViewHolder, position: Int) {
        val list = list[position]
        holder.binding.tvDesc.text = list.descriptions.toString()
        holder.binding.userName.text = list.user?.name.toString()
        holder.binding.imgPost.visibility
        if (list.content != null){
            Glide.with(holder.itemView.context)
                .load("${list.content}")
                .placeholder(R.drawable.dummy_post_img)
                .into(holder.binding.imgPost)
        } else {
            holder.binding.imgPost.isGone = true
        }
        Glide.with(holder.itemView.context)
            .load("${list.user?.avatar}")
            .placeholder(R.drawable.dummy_avatar)
            .into(holder.binding.userProfile)
    }

    override fun getItemCount(): Int = list.size

}