package com.qatros.samakaki.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.qatros.samakaki.R
import com.qatros.samakaki.databinding.LayoutPostContentBinding
import com.qatros.samakaki.response.Data
import com.qatros.samakaki.response.DataItem
import com.squareup.picasso.Picasso

class PostsAdapter(private var list: List<DataItem>, private var optionsMenuClickListener: OptionsMenuClickListener) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    val myData = mutableListOf<DataItem>()

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int, id: Int?)
    }

    inner class PostsViewHolder (val binding: LayoutPostContentBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val binding = LayoutPostContentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val list = list[position]
        holder.binding.tvDesc.text = list.descriptions.toString()
        holder.binding.userName.text = list.user?.name.toString()
        holder.binding.textViewOptions.setOnClickListener {
            optionsMenuClickListener.onOptionsMenuClicked(position, list.id)
        }
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

    fun submitList(newData: List<DataItem>) {
        myData.clear()
        myData.addAll(newData)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = list.size


}