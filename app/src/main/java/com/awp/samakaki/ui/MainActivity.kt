package com.awp.samakaki.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivityMainBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.helper.SessionManager.INVITATION_TOKEN
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.viewmodel.FamilyTreeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val familyTreeViewModel by viewModels<FamilyTreeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        val isiTokenInvit = SessionManager.getInvitation(this)
        Log.d("isi_token_invitation", isiTokenInvit.toString())

        val tokenLogin = SessionManager.getToken(this)
        Log.d("isi_token_login", "token $tokenLogin")


        if (!isiTokenInvit.isNullOrBlank()) {

            familyTreeViewModel.inviteFamily("Bearer $tokenLogin", isiTokenInvit.toString())
            familyTreeViewModel.inviteFamily.observe(this) {
                when(it) {
                    is BaseResponse.Success -> {
                        SessionManager.removeInvitationToken(this)
                        Toast.makeText(this, "Anda mendapat notifikasi baru", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "onCreateSuccess: " + it.data?.data)
                        Log.d("isi_token_after_delete", isiTokenInvit.toString())
                    }

                    is BaseResponse.Error -> {
                        Toast.makeText(this, it.msg.toString(), Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "onCreate: " + it.msg.toString())
                    }

                }
            }
        }

    }

}