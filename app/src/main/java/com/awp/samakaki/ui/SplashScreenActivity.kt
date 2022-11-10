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
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivitySplashScreenBinding
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

        }

        Handler(Looper.getMainLooper()).postDelayed({


            val getToken = SessionManager.getToken(this)
            val getId = SessionManager.getIdUser(this)
            val token = SessionManager.getToken(this)


            if (!token.isNullOrEmpty()) {

                profileViewModel.findUser(token = "Bearer $getToken!!", id = getId.toString())
                profileViewModel.findUser.observe(this) {
                    when(it) {
                        is BaseResponse.Success -> {
                            it.data
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
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


        }, 3000)

    }
}