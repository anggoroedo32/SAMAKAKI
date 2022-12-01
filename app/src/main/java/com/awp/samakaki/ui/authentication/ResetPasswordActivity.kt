package com.awp.samakaki.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivityResetPasswordBinding
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingState()

        val btnUbahPassword = binding.btnUbahPassword
        btnUbahPassword.setOnClickListener {

            val email = binding.etEmail.text.trim().toString()
            val password = binding.etPassword.text.trim().toString()
            val token = binding.etToken.text.trim().toString()

            when {
                email.isEmpty() -> {
                    binding.etEmail.error = getString(R.string.er_empty_email)
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.etEmail.error = getString(R.string.err_wrong_email_format)
                }
                password.isEmpty() -> {
                    binding.etPassword.error = getString(R.string.err_empty_password)
                }
                token.isEmpty() -> {
                    binding.etToken.error = getString(R.string.err_empty_token)
                }
                else -> {
                    authenticationViewModel.resetPassword(
                        email = email,
                        password = password,
                        token = token
                    )
                    authenticationViewModel.resetPasswordResponse.observe(this) {
                        when (it) {
                            is BaseResponse.Success -> {
                                textMessage("Password berhasil diubah")
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            }
                            is BaseResponse.Error -> {
                                textMessage(it.msg.toString())
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
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}