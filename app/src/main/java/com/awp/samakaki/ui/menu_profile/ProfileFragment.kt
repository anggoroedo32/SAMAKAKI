package com.awp.samakaki.ui.menu_profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.R
import com.awp.samakaki.adapter.PostsAdapter
import com.awp.samakaki.adapter.UserPostsAdapter
import com.awp.samakaki.databinding.FragmentProfileBinding
import com.awp.samakaki.helper.ConnectivityStatus
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.DataItem
import com.awp.samakaki.response.ItemPosts
import com.awp.samakaki.ui.MainActivity
import com.awp.samakaki.viewmodel.NotificationsViewModel
import com.awp.samakaki.viewmodel.PostsViewModel
import com.awp.samakaki.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import ru.nikartm.support.ImageBadgeView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PostsViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()
    private var imageBadgeView: ImageBadgeView? = null
    private val notificationsViewModel by viewModels<NotificationsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkConnectivity()

        val toolbar = binding.toolbarHomepage
        toolbar.inflateMenu(R.menu.menu_home)
        initNotificationCounter()
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
//                R.id.notification -> findNavController().navigate(R.id.action_navigation_home_to_notificationsFragment)
                R.id.settings -> findNavController().navigate(R.id.action_navigation_profile_to_settingsFragment)
            }
            true
        }


        val name = binding.TVProfilename
        val address = binding.tvAlamat
        val phone = binding.tvNohp
        val mariageStatus = binding.tvStatus
        val avatar = binding.imgProfile


        val token = context?.let { SessionManager.getToken(it) }
        val id = SessionManager.getIdUser(requireContext())
        Log.d("id_dari_edit_profile", id.toString())
        profileViewModel.findUser(token = "Bearer $token!!", id = id.toString())
        profileViewModel.findUser.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {
                    it.data

                    name.setText(it.data?.data?.biodata?.name)
                    if (it.data?.data?.biodata?.dob?.isNotEmpty() == true) {
                        formatDate(it.data?.data?.biodata?.dob.toString())
                    } else if (it.data?.data?.biodata?.dob?.isNullOrBlank() == true) {
                        binding.tvTgllahir.text = it.data?.data?.biodata?.dob?.toString()
                    }
                    var addressCapitalize = it.data?.data?.biodata?.address
                    address.setText(addressCapitalize?.capitalize())
                    phone.setText(it.data?.data?.biodata?.phone)
                    val marriageStatusCapitalize = it.data?.data?.biodata?.marriageStatus
                    mariageStatus.setText(marriageStatusCapitalize?.capitalize())

                    Picasso.get()
                        .load(it.data?.data?.biodata?.avatar)
                        .fit()
                        .centerInside()
                        .error(R.drawable.dummy_avatar)
                        .into(avatar)

                }
                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }

        loadingState()

        val btnEdProfile = binding.btnEdProfile
        btnEdProfile.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_edit_profile_fragment)
        }

        //Get Post Profile
        getPosts()
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

        imageBadgeView?.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_notificationsFragment)
        }
    }

    private fun checkConnectivity() {
        val connectivity = ConnectivityStatus(requireContext())
        connectivity.observe(viewLifecycleOwner) {
                isConnected ->
            if(!isConnected){
                textMessageLong("Tidak ada koneksi internet")
            }
        }
    }

    private fun formatDate(inputDate: String) {
        var inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        var dateFormater: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        var date: Date = inputFormat.parse(inputDate)
        var outputDate: String = dateFormater.format(date)
        binding.tvTgllahir.text = outputDate
    }

    private fun observeData(){
        viewModel.listAllPostsByUser.observe(viewLifecycleOwner) {
            when(it){
                is BaseResponse.Success -> {
                    it.data?.data?.posts.let { it1 -> rvPosts(it1 as List<ItemPosts>) }
                }
                is BaseResponse.Error -> {
                    textMessage(it.msg.toString())
                }
            }
        }
    }
    private fun rvPosts(list: List<ItemPosts>) {
        val recyclerViewPosts: RecyclerView = binding.rvProfile
        recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            adapter = UserPostsAdapter(list)
        }
    }

    private fun getPosts(){
        var tokenGet = SessionManager.getToken(requireContext())
        viewModel.getAllPostsByUser("bearer $tokenGet")
        observeData()
    }

    private fun textMessage(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }

    private fun textMessageLong(s: String) {
        Toast.makeText(requireContext(),s, Toast.LENGTH_LONG).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadingState(){
        viewModel.loading.observe(viewLifecycleOwner){
            binding.prgbar.isVisible = !it
            binding.prgbar.isVisible = it
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.prgbar.isVisible = !it
            binding.prgbar.isVisible = it
        }
    }

}