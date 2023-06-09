package com.qatros.samakaki.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatDelegate
import com.qatros.samakaki.R
import com.qatros.samakaki.databinding.ActivitySplashScreenBinding
import com.qatros.samakaki.helper.ConnectivityStatus
import com.qatros.samakaki.helper.SessionManager
import com.qatros.samakaki.response.BaseResponse
import com.qatros.samakaki.ui.authentication.LoginActivity
import com.qatros.samakaki.ui.authentication.RegisterActivity
import com.qatros.samakaki.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var messageTV: TextView
    var invitToken : String? = null
    private var uri: Uri? = null
    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkConnectivity()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        messageTV = findViewById(R.id.tv_token)
        uri = intent.data

        if (uri != null) {

            val parameters = uri!!.pathSegments
            val param = parameters[parameters.size - 1]

            messageTV.text = param
            invitToken = param

            val tokenInvitation = invitToken
            SessionManager.saveInvitation(this, tokenInvitation.toString())

        }

    }

    private fun checkConnectivity() {
        val connectivity = ConnectivityStatus(this)
        connectivity.observe(this, Observer {
                isConnected ->
            if(isConnected){
                doCheck()
            }else{
                Toast.makeText(this, "Tidak ada koneksi internet" ,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun doCheck() {
        Handler(Looper.getMainLooper()).postDelayed({


            val getId = SessionManager.getIdUser(this)
            val token = SessionManager.getToken(this)


            if (!token.isNullOrEmpty()) {

                profileViewModel.findUser(token = "Bearer $token", id = getId.toString())
                profileViewModel.findUser.observe(this) {
                    when(it) {
                        is BaseResponse.Success -> {
                            val biodata = it.data?.data?.biodata
                            if (biodata == null){
                                val intent = Intent(this, SelamatDatangActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this, MainActivity::class.java)
//                                val intent = Intent(this, SelamatDatangActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                            }
                        }
                        is BaseResponse.Error -> {
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }
                    }
                }

            } else if (token.isNullOrEmpty()) {

                val intent = Intent(this, RegisterActivity::class.java)
                intent.putExtra("invit", invitToken)
                startActivity(intent)
                finish()
            }


        }, 2000)
    }

}