package com.awp.samakaki.ui.menu_beranda

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.awp.samakaki.databinding.FragmentChatListBinding
import com.awp.samakaki.databinding.FragmentSettingsBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.ui.authentication.LoginActivity


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get()= _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSettingProfile = binding.btnSettingProfile
        val btnSettingPrivacy = binding.btnSettingPrivacy
        val btnSettingAbout = binding.btnSettingAbout
        val btnBack = binding.btnBack
        val btnLogout = binding.btnLogout

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        btnLogout.setOnClickListener {
            SessionManager.clearData(requireContext())
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }

}