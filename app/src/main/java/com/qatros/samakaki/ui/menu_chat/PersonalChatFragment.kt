package com.qatros.samakaki.ui.menu_chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qatros.samakaki.R

class PersonalChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var recyclerLayoutManager: RecyclerView.LayoutManager? = null
    private var recyclerAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerLayoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.rv_chat_personal)
        recyclerView.layoutManager = recyclerLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerAdapter = RecyclerViewAdapter()
        recyclerView.adapter = recyclerAdapter

    }


}