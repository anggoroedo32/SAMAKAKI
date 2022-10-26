package com.awp.samakaki.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.databinding.LayoutPostContentBinding
import com.awp.samakaki.response.PostItem
import com.bumptech.glide.Glide

class PostsAdapter(private var list: List<PostItem>) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    inner class PostsViewHolder (val binding: LayoutPostContentBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val binding = LayoutPostContentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val list = list[position]
        holder.binding.tvDesc.text = list!!.descriptions
//        Glide.with(holder.itemView.context)
//            .load(list.link)
//            .centerCrop()
//            .into(holder.binding.imgPost)
    }


    override fun getItemCount(): Int = list.size


//    fun setListPostsData(listDataPost: List<PostItem>)
//    {
//        this.list = listDataPost
//        notifyDataSetChanged()
//    }

}