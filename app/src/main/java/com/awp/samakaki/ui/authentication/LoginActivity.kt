package com.awp.samakaki.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivityLoginBinding
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.ui.SelamatDatangActivity
import com.awp.samakaki.viewmodel.AuthenticationViewModel
import com.awp.samakaki.viewmodel.PostsViewModel
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

            if (email.isEmpty()){
                binding.etEmail.error = getString(R.string.er_empty_email)
            }
            if (password.isEmpty()){
                binding.etPassword.error = getString(R.string.err_empty_password)
            }

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
                    startActivity(Intent(this, SelamatDatangActivity::class.java))
//                    authenticationViewModel.login(email = email, password = password)
//                    authenticationViewModel.loginResponse.observe(this) {
//                       when(it) {
//                           is BaseResponse.Success -> startActivity(Intent(this, SelamatDatangActivity::class.java))
//                           is BaseResponse.Error -> textMessage(it.msg.toString())
//                       }
//                    }
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
    private fun textMessage(s: String) {
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show()
    }
}