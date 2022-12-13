package com.qatros.samakaki.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.qatros.samakaki.R
import com.qatros.samakaki.databinding.ActivityMainBinding
import com.qatros.samakaki.helper.SessionManager
import com.qatros.samakaki.response.BaseResponse
import com.qatros.samakaki.viewmodel.FamilyTreeViewModel
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
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            if (
                destination.id == R.id.navigation_profile ||
                destination.id == R.id.navigation_home ||
                destination.id == R.id.navigation_family
            ) {
                navView.visibility = View.VISIBLE
            } else {
                navView.visibility = View.GONE
            }
        }

        val isiTokenInvit = SessionManager.getInvitation(this)
        if (isiTokenInvit != null && isiTokenInvit.length > 4) {
            Log.d("isi_token_invitation", isiTokenInvit)
        }

        val tokenLogin = SessionManager.getToken(this)
        Log.d("isi_token_login", "token $tokenLogin")


        if (isiTokenInvit != null && isiTokenInvit.length > 4) {
            Log.d("TAG", "onCreate: $isiTokenInvit")
            familyTreeViewModel.inviteFamily("Bearer $tokenLogin", isiTokenInvit.toString())
            familyTreeViewModel.inviteFamily.observe(this) {
                when(it) {
                    is BaseResponse.Success -> {
                        SessionManager.removeInvitationToken(this)
                        Toast.makeText(this, "Anda mendapat notifikasi baru", Toast.LENGTH_SHORT).show()
                    }

                    is BaseResponse.Error -> {
                        Toast.makeText(this, "masih ngehit", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

    }

}