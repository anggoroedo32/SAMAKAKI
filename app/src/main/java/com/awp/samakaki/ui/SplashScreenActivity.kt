package com.awp.samakaki.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivitySplashScreenBinding
import com.awp.samakaki.helper.AppStatus
import com.awp.samakaki.helper.ConnectivityStatus
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.ui.authentication.LoginActivity
import com.awp.samakaki.ui.authentication.RegisterActivity
import com.awp.samakaki.viewmodel.ProfileViewModel
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
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                            }
                        }
                        is BaseResponse.Error -> {
                            val intent = Intent(this, SelamatDatangActivity::class.java)
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