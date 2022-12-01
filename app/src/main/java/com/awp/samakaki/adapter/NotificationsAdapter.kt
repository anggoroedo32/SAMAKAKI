package com.awp.samakaki.adapter

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.databinding.CardviewInvitationNotificationBinding
import com.awp.samakaki.response.UnreadItem


class NotificationsAdapter (private var list: List<UnreadItem>, val context: Context) : RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {

    inner class NotificationsViewHolder (val binding: CardviewInvitationNotificationBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsAdapter.NotificationsViewHolder {
        val binding = CardviewInvitationNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val list = list[position]

        val data: MutableList<String> = ArrayList()
        data.add("list 1")
        data.add("list 2")
        data.add("list 3")

        val spinnerData = context.resources.getStringArray(com.awp.samakaki.R.array.relation)
        val relationAdapter = ArrayAdapter(context, R.layout.simple_spinner_dropdown_item, spinnerData)
        holder.binding.relation.adapter = relationAdapter
        val selection = list.relation
        val spinnerPosition: Int = relationAdapter.getPosition(selection)
        holder.binding.relation.setSelection(spinnerPosition)

        if (list.descriptions!!.contains("sudah diterima oleh")) {
            holder.binding.wrapRegardAs.visibility = View.GONE
        }

        holder.binding.username.text = list.invitingName
        holder.binding.notifMessage.text = list.descriptions
        holder.binding.invitToken.text = list.invitationToken
        holder.binding.ibAccept.setOnClickListener {
            listener?.onCard(list)
        }
    }

    var listener: ibAcceptClickListener? = null

    override fun getItemCount(): Int = list.size

}

interface ibAcceptClickListener {
    fun onCard(movie: UnreadItem)
}