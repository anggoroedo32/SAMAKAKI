package com.qatros.samakaki.ui.menu

import android.content.Intent
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
import com.qatros.samakaki.R
import com.qatros.samakaki.databinding.FragmentSettingPrivacyBinding
import com.qatros.samakaki.helper.SessionManager
import com.qatros.samakaki.response.BaseResponse
import com.qatros.samakaki.ui.MainActivity
import com.qatros.samakaki.ui.SelamatDatangActivity
import com.qatros.samakaki.ui.authentication.LoginActivity
import com.qatros.samakaki.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
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

        val btnBack = binding.btnBack
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val token = SessionManager.getToken(requireContext())
        val idUser = SessionManager.getIdUser(requireContext())
        profileViewModel.findUser(token = "Bearer $token", id = idUser.toString())
        profileViewModel.findUser.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {
                    val dataItem = it.data?.data?.biodata?.status
                    var position = 0
                    if (dataItem == "private"){
                        position = R.id.radio_button1
                    } else if (dataItem == "public") {
                        position = R.id.radio_button2
                    } else if (dataItem == "protected") {
                        position = R.id.radio_button3
                    }
                    binding.radioGroup.check(position)
                }
                is BaseResponse.Error -> {
                    textMessage(it.msg.toString())
                }
            }
        }

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
            Log.d("TAG", "onViewCreatedBtn: $_radioButton")
//            _radioButton.let { it1 -> insertEditPrivacy(it1) }
            insertEditPrivacy(_radioButton)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
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
                    textMessage("Status anda sudah diperbarui")
                    val destination = findNavController().currentDestination?.id
                    findNavController().popBackStack(destination!!,true)
                    findNavController().navigate(destination)
                    Toast.makeText(requireContext(), "Berhasil Ganti Privasi", Toast.LENGTH_SHORT).show()
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