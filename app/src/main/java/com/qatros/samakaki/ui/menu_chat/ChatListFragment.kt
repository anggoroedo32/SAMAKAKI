package com.qatros.samakaki.ui.menu_chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.qatros.samakaki.R
import com.qatros.samakaki.databinding.FragmentChatListBinding
import com.qatros.samakaki.helper.SessionManager
import com.qatros.samakaki.response.BaseResponse
import com.qatros.samakaki.viewmodel.NotificationsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.nikartm.support.ImageBadgeView

@AndroidEntryPoint
class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding get()= _binding!!
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private val notificationsViewModel by viewModels<NotificationsViewModel>()

    private var imageBadgeView: ImageBadgeView? = null
    var notificationsCount: Int = 0


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
        initNotificationCounter()
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
//                R.id.notification -> findNavController().navigate(R.id.action_navigation_chat_to_notificationsFragment)
//                R.id.settings -> findNavController().navigate(R.id.action_navigation_chat_to_settingsFragment)
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

    private fun initNotificationCounter() {
        val token = SessionManager.getToken(requireContext())
        imageBadgeView = view?.findViewById(R.id.notification_menu_icon)

        notificationsViewModel.getNotifications("Bearer $token")
        notificationsViewModel.getNotifications.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {
                    imageBadgeView?.badgeValue = it.data?.data?.unread?.size!!
                }

                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }

//        imageBadgeView?.setBadgeValue(notificationsCount)
//            ?.setMaxBadgeValue(99)
//            ?.setLimitBadgeValue(true)

//        imageBadgeView?.setOnClickListener {
//            findNavController().navigate(R.id.action_navigation_chat_to_notificationsFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun textMessage(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }

}