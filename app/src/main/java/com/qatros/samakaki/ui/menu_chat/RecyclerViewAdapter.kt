package com.qatros.samakaki.ui.menu_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.qatros.samakaki.R

class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var username = arrayOf("Bambang Supriyanto", "Arif Wahyu", "Muhammad Sumbul", "Bima Aria",
        "Ahmad Santosa", "Ikhwan Inzaghi", "Anggoro Edo", "Ismail Al Khanabawi", "Mario Kurniawan")
    var chat = arrayOf("Ingpo madang", "Ingpo madang", "Ingpo madang", "Ingpo madang",
        "Ingpo madang", "Ingpo madang", "Ingpo madang", "Ingpo madang", "Ingpo madang")
    var hour = arrayOf("12.10", "12.10", "12.10", "12.10", "12.10", "12.10", "12.10", "12.10", "12.10")
    var pp = arrayOf(
        R.drawable.bg_profile, R.drawable.bg_profile, R.drawable.bg_profile,
        R.drawable.bg_profile, R.drawable.bg_profile, R.drawable.bg_profile, R.drawable.bg_profile,
        R.drawable.bg_profile, R.drawable.bg_profile
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.cardview_chat_list, parent, false)

        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvUsername.text = username[position]
        holder.tvChat.text = chat[position]
        holder.tvHour.text = hour[position]
        holder.ibProfile.setImageResource(pp[position])

    }

    override fun getItemCount(): Int {
        return username.size
    }

    inner class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview) {
        var ibProfile: ImageButton
        var tvUsername: TextView
        var tvChat: TextView
        var tvHour: TextView

        init {
            ibProfile = itemview.findViewById(R.id.ib_profile)
            tvUsername = itemview.findViewById(R.id.tv_username)
            tvChat = itemview.findViewById(R.id.tv_chat)
            tvHour = itemview.findViewById(R.id.tv_hour)
        }
    }

}