package com.awp.samakaki.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.databinding.CardviewInvitationNotificationBinding
import com.awp.samakaki.response.UnreadItem
import com.awp.samakaki.viewmodel.FamilyTreeViewModel


class NotificationsAdapter (private var list: List<UnreadItem>) : RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {

    inner class NotificationsViewHolder (val binding: CardviewInvitationNotificationBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsAdapter.NotificationsViewHolder {
        val binding = CardviewInvitationNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val list = list[position]
        holder.binding.username.text = list!!.invitingName
        holder.binding.notifMessage.text = list!!.descriptions
        holder.binding.relation.text = list!!.relation
        holder.binding.invitToken.text = list!!.invitationToken
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