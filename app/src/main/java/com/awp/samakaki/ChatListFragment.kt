package com.awp.samakaki

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.databinding.FragmentChatListBinding
import com.google.android.material.tabs.TabLayoutMediator


class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding get()= _binding!!
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = ViewPagerAdapter(this)
        val toolbar = binding.toolbarHomepage

        toolbar.inflateMenu(R.menu.menu_home)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.notification -> Toast.makeText(context, "Clicked Notification", Toast.LENGTH_SHORT).show()
                R.id.settings -> Toast.makeText(context, "Clicked Setting", Toast.LENGTH_SHORT).show()
            }
            true
        }


        with(binding){
            vpChatList.adapter = viewPagerAdapter

            TabLayoutMediator(tlChatList, vpChatList) { tab, position ->
                when(position){
                    0 -> tab.text = "Personal"
                    1 -> tab.text = "Keluarga"
                }
            }.attach()

            for (i in 0..2) {
                val textView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_layout, null)
                    as TextView
                binding.tlChatList.getTabAt(i)?.customView = textView
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}