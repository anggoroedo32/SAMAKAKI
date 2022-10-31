package com.awp.samakaki.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
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

        val btnUbahPassword = binding.btnUbahPassword
        btnUbahPassword.setOnClickListener {

            val email = binding.etEmail.text.trim().toString()
            val password = binding.etPassword.text.trim().toString()
            val token = binding.etToken.text.trim().toString()

            authenticationViewModel.resetPassword(email = email, password = password, token = token)
            authenticationViewModel.resetPasswordResponse.observe(this) {
                when(it) {
                    is BaseResponse.Loading -> {
                        showLoading()
                    }
                    is BaseResponse.Success -> {
                        stopLoading()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        textMessage("Password berhasil diubah")
                    }
                    is BaseResponse.Error -> {
                        textMessage(it.msg.toString())
                    }
                }
            }
        }

    }

    fun stopLoading() {
        binding.prgbar.visibility = View.GONE
    }

    fun showLoading() {
        binding.prgbar.visibility = View.VISIBLE
    }

    private fun textMessage(s: String) {
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show()
    }
}