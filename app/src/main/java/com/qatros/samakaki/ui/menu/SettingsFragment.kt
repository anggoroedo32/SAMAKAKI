package com.qatros.samakaki.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.qatros.samakaki.R
import com.qatros.samakaki.databinding.FragmentSettingsBinding
import com.qatros.samakaki.helper.SessionManager
import com.qatros.samakaki.ui.authentication.LoginActivity


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
            if (!findNavController().popBackStack()) {
                findNavController().navigate(R.id.navigation_home)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (!findNavController().popBackStack()) {
                    findNavController().navigate(R.id.navigation_home)
                }
            }
        })


        btnLogout.setOnClickListener {
            SessionManager.clearData(requireContext())
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        btnSettingPrivacy.setOnClickListener {
            findNavController().navigate(R.id.settingPrivacyFragment)
        }

        btnSettingAbout.setOnClickListener {
            findNavController().navigate(R.id.aboutFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}