package com.awp.samakaki.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivityForgotPasswordBinding
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                            when (it) {
                                is BaseResponse.Loading -> {
                                    showLoading()
                                }
                                is BaseResponse.Success -> {
                                    stopLoading()
                                    textMessage("Token sudah dikirimkan ke email anda")
                                    val intent = Intent(this, ResetPasswordActivity::class.java)
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

