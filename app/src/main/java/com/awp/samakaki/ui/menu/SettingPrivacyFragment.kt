package com.awp.samakaki.ui.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.awp.samakaki.R
import com.awp.samakaki.databinding.FragmentSettingPrivacyBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.viewmodel.ProfileViewModel
import dagger.hilt.android.internal.Contexts.getApplication
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


class SettingPrivacyFragment : Fragment() {
    private var _binding: FragmentSettingPrivacyBinding? = null
    private val binding get()  = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    private var _radioGroup: RadioGroup? = null
    private var _radioButton: String = "private"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingPrivacyBinding.inflate(layoutInflater)
        val root = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _radioGroup = binding.radioGroup
        _radioGroup!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{ radioGroup, id ->
            when(id){
                R.id.radio_button1 -> {
                    _radioButton = "private"
                    Log.d("TAG", "onViewCreated: $_radioButton")
                }
                R.id.radio_button2 -> {
                    _radioButton = "public"
                    Log.d("TAG", "onViewCreated: $_radioButton")
                }
                R.id.radio_button3 -> {
                    _radioButton = "protected"
                }
            }
        })


        binding.btnSave.setOnClickListener{
            _radioButton.let { it1 -> insertEditPrivacy(it1) }
        }
    }

    private fun insertEditPrivacy(status: String){
        val token = SessionManager.getToken(requireContext())
        val id = SessionManager.getIdUser(requireContext())
        var isStatus = status.toRequestBody("text/plain".toMediaType())

        profileViewModel.editPrivacy("bearer $token",id,isStatus)
        profileViewModel.editPrivacy.observe(viewLifecycleOwner) {
            when(it){
                is BaseResponse.Success -> {
                    it.data
                    val destination = findNavController().currentDestination?.id
                    findNavController().popBackStack(destination!!,true)
                    findNavController().navigate(destination)
                }
                is BaseResponse.Error -> {
                    textMessage(it.msg.toString())
                }
            }
        }
    }

    private fun textMessage(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }
    companion object{

    }
}