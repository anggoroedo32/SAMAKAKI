package com.awp.samakaki

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment)
    : FragmentStateAdapter(fragment)  {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
         var fragment = Fragment()
        when(position){
            0 -> fragment = PersonalChatFragment()
            1 -> fragment = FamilyChatFragment()
        }
        return fragment
    }


}