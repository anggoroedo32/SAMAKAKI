package com.qatros.samakaki.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.qatros.samakaki.R
import com.qatros.samakaki.databinding.ActivityForgotPasswordBinding
import com.qatros.samakaki.response.BaseResponse
import com.qatros.samakaki.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingState()

        val btnKonfirmasi = binding.btnKonfirmasi
        btnKonfirmasi.setOnClickListener {

            val email = binding.etEmail.text.trim().toString()

            when {
                email.isEmpty() -> {
                    binding.etEmail.error = getString(R.string.er_empty_email)
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.etEmail.error = getString(R.string.err_wrong_email_format)
                }
                    else -> {
                        authenticationViewModel.forgotToken(email = email)
                        authenticationViewModel.forgotTokenResponse.observe(this) {
                            it.getContentIfNotHandled().let {
                                when(it) {
                                    is BaseResponse.Success -> {
                                        textMessage("Token sudah dikirimkan ke email anda")
                                        val intent = Intent(this, ResetPasswordActivity::class.java)
                                        startActivity(intent)
                                    }
                                    is BaseResponse.Error -> {
                                        textMessage(it.msg.toString())
                                        Log.d("TAG", "onCreate: ${it.msg.toString()}")
                                    }
                                    else -> {}
                                }
                            }
                        }
                    }
            }

        }

        val btnBack = binding.icBack
        btnBack.setOnClickListener {
            finish()
        }

    }

    private fun loadingState(){
        authenticationViewModel.loading.observe(this){
            binding.prgbar.isVisible = !it
            binding.prgbar.isVisible = it
        }
    }

    private fun textMessage(s: String) {
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show()
    }

}

