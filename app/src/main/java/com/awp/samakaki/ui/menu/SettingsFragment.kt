package com.awp.samakaki.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.awp.samakaki.R
import com.awp.samakaki.databinding.FragmentSettingsBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.ui.authentication.LoginActivity
import com.awp.samakaki.ui.menu_profile.EditProfileFragment


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
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        btnSettingPrivacy.setOnClickListener {
            findNavController().navigate(R.id.settingPrivacyFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}