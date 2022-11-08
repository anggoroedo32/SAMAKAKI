package com.awp.samakaki.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.awp.samakaki.R
import com.awp.samakaki.adapter.PostsAdapter
import com.awp.samakaki.databinding.ActivitySelamatDatangBinding
import com.awp.samakaki.databinding.FragmentProfileBinding
import com.awp.samakaki.viewmodel.IsiProfilViewModel
import com.awp.samakaki.viewmodel.PostsViewModel
import com.awp.samakaki.viewmodel.ProfileViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var ivUploadImg: ImageView
    private val calendar = Calendar.getInstance()
    private var dateFormater: String? = null
    private var imageFile: File? = null

    private val viewModel by viewModels<ProfileViewModel>()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}