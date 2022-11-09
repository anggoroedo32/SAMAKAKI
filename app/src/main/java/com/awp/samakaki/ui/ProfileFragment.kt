package com.awp.samakaki.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.awp.samakaki.R
import com.awp.samakaki.adapter.PostsAdapter
import com.awp.samakaki.databinding.ActivitySelamatDatangBinding
import com.awp.samakaki.databinding.FragmentProfileBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.viewmodel.IsiProfilViewModel
import com.awp.samakaki.viewmodel.PostsViewModel
import com.awp.samakaki.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
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
                    phone.setText(it.data?.data?.biodata?.phone)
                    mariageStatus.setText(it.data?.data?.biodata?.marriageStatus)

                    Glide.with(this)
                        .load(it.data?.data?.biodata?.avatar)
                        .centerInside()
                        .placeholder(R.drawable.dummy_avatar).error(R.drawable.dummy_avatar)
                        .into(avatar)

                }
                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }

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
    }

    private fun textMessage(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}