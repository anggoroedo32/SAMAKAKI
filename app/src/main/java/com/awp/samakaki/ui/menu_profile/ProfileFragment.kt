package com.awp.samakaki.ui.menu_profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.R
import com.awp.samakaki.adapter.PostsAdapter
import com.awp.samakaki.databinding.FragmentProfileBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.DataItem
import com.awp.samakaki.viewmodel.PostsViewModel
import com.awp.samakaki.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PostsViewModel>()
    private lateinit var ivUploadImg: ImageView
    private val calendar = Calendar.getInstance()
    private var dateFormater: String? = null
    private var imageFile: File? = null

    private val profileViewModel by viewModels<ProfileViewModel>()
    private lateinit var postsAdapter: PostsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        dateFormater = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = binding.TVProfilename
        val dob = binding.tvTgllahir
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
                    dob.setText(it.data?.data?.biodata?.dob)
                    var addressCapitalize = it.data?.data?.biodata?.address
                    address.setText(addressCapitalize?.capitalize())
                    phone.setText(it.data?.data?.biodata?.phone)
                    val marriageStatusCapitalize = it.data?.data?.biodata?.marriageStatus
                    mariageStatus.setText(marriageStatusCapitalize?.capitalize())

//                    Glide.with(this)
//                        .load(it.data?.data?.biodata?.avatar)
//                        .centerInside()
//                        .placeholder(R.drawable.dummy_avatar).error(R.drawable.dummy_avatar)
//                        .into(avatar)

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
            val fragmentManager = parentFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(
                R.id.nav_host_fragment_activity_main,
                EditProfileFragment()
            )
                .setReorderingAllowed(true)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        //Get Post Profile
        getPosts()
    }

    private fun observeData(){
        viewModel.listAllPosts.observe(viewLifecycleOwner) {
            when(it){
                is BaseResponse.Success -> {
                    it.data?.data?.let { it1 -> rvPosts(it1) }
                }
                is BaseResponse.Error -> {
                    textMessage(it.msg.toString())
                }
            }
        }
    }
    private fun rvPosts(list: List<DataItem>) {
        val recyclerViewPosts: RecyclerView = binding.rvProfile
        recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = PostsAdapter(list)
        }
    }

    private fun getPosts(){
        var tokenGet = SessionManager.getToken(requireContext())
        viewModel.getAllPostsByFamily("bearer $tokenGet")
        observeData()
    }

    private fun textMessage(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
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