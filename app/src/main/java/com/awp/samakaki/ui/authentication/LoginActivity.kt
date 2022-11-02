package com.awp.samakaki.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivityLoginBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.LoginResponse
import com.awp.samakaki.ui.SelamatDatangActivity
import com.awp.samakaki.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val btnLogin = binding.btnLogin
        btnLogin.setOnClickListener {

            val email = binding.etEmail.text.trim().toString()
            val password = binding.etPassword.text.trim().toString()

            when{
                email.isEmpty() -> {
                    binding.etEmail.error = getString(R.string.er_empty_email)
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.etEmail.error = getString(R.string.err_wrong_email_format)
                }
                password.isEmpty() -> {
                    binding.etPassword.error = getString(R.string.err_empty_password)
                }
                else -> {
                    authenticationViewModel.login(email = email, password = password)
                    authenticationViewModel.loginResponse.observe(this) {
                        Log.d("data_login_activity", "data outside when ${it}")
                       when(it) {
                           is BaseResponse.Loading -> {
                               showLoading()
                           }
                           is BaseResponse.Success -> {
                               stopLoading()
                               processLogin(it.data)
                           }
                           is BaseResponse.Error -> {
                               textMessage(it.msg.toString())
                           }
                       }
                    }
                }
            }

        }

        val tvRegister = binding.txtViewRegister
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val tvForgotPassword = binding.tvForgotPassword
        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }


    }

    private fun navigateToHome() {
        val intent = Intent(this, SelamatDatangActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    fun processLogin(data: LoginResponse?) {
        textMessage("Login berhasil")
        val token = data?.dataLogin?.token
        if (!token.isNullOrEmpty()) {
            token.let { SessionManager.saveAuthToken(this, it) }
            navigateToHome()
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