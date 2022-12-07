package com.awp.samakaki.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.R
import com.awp.samakaki.databinding.LayoutPostContentBinding
import com.awp.samakaki.response.DataItem
import com.awp.samakaki.response.PostsItem
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class PostsAdapter(private var list: List<DataItem>) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    inner class PostsViewHolder (val binding: LayoutPostContentBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val binding = LayoutPostContentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val list = list[position]
        holder.binding.tvDesc.text = list.descriptions.toString()
        holder.binding.userName.text = list.user?.name.toString()
        holder.binding.imgPost.visibility

        if (list.content != null){

            Picasso.get()
                .load("${list.content}")
                .error(R.drawable.dummy_post_img)
                .into(holder.binding.imgPost)
            
        } else {
            holder.binding.imgPost.isGone = true
        }

        if (list.descriptions.isNullOrBlank()) {
            holder.binding.tvDesc.visibility = View.GONE
        }

        Picasso.get()
            .load("${list.user?.avatar}")
            .fit()
            .centerInside()
            .error(R.drawable.dummy_avatar)
            .into(holder.binding.userProfile)

    }


    override fun getItemCount(): Int = list.size


}