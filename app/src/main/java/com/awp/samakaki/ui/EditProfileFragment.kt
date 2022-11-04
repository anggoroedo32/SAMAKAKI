package com.awp.samakaki.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.awp.samakaki.R
import com.awp.samakaki.databinding.FragmentEditprofileBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.viewmodel.ProfileViewModel
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditprofileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditprofileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val statusDropdown = resources.getStringArray(R.array.status)
        val statusDropdownAdapter =
            context?.let { ArrayAdapter(it,R.layout.dropdown_item,statusDropdown) }
        val autoCompleteStatus = binding.etStatus
        autoCompleteStatus.setAdapter(statusDropdownAdapter)

        val token = context?.let { SessionManager.getToken(it) }
        val id = SessionManager.getIdUser(requireContext())
        Log.d("id_dari_edit_profile", id.toString())
        profileViewModel.findUser(token = "Bearer $token!!", id = id.toString())
        profileViewModel.findUser.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {
                    it.data
                }
                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val fragmentManager = parentFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(
                    R.id.nav_host_fragment_activity_main,
                    ProfileFragment()
                )
                    .setReorderingAllowed(true)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

        })
    }

    private fun textMessage(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }

}