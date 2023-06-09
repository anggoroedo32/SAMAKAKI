package com.qatros.samakaki.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.qatros.samakaki.R
import com.qatros.samakaki.databinding.ActivityLoginBinding
import com.qatros.samakaki.helper.SessionManager
import com.qatros.samakaki.response.BaseResponse
import com.qatros.samakaki.response.LoginResponse
import com.qatros.samakaki.ui.MainActivity
import com.qatros.samakaki.ui.SelamatDatangActivity
import com.qatros.samakaki.viewmodel.AuthenticationViewModel
import com.qatros.samakaki.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()

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
                        it.getContentIfNotHandled()?.let {
                            when(it) {
                                is BaseResponse.Success -> {
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

        }

        loadingState()

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

    fun processLogin(data: LoginResponse?) {
        val token = data?.dataLogin?.token
        val id = data?.dataLogin?.users?.id
        SessionManager.saveIdUser(this, id!!)
        Log.d("id_dari_login", "isinya $id")
        if (!token.isNullOrEmpty()) {
            token.let { SessionManager.saveAuthToken(this, it) }
            navigateToHome()
        }


    }

    private fun navigateToHome() {
        val getToken = SessionManager.getToken(this)
        val getId = SessionManager.getIdUser(this)
        Log.d("tokendari_login", "isinya $getId")
        Log.d("idnavigate_dari_login", "isinya $getToken")

        profileViewModel.findUser(token = "Bearer $getToken!!", id = getId.toString())
        profileViewModel.findUser.observe(this) {
            when(it) {
                is BaseResponse.Success -> {
                    it.data

                    if (it.data?.data?.biodata == null) {
                        val intent = Intent(this, SelamatDatangActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                }
                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }

    }

    private fun loadingState(){
        authenticationViewModel.loading.observe(this){
            binding.prgbar.isVisible = !it
            binding.prgbar.isVisible = it
        }

        profileViewModel.loading.observe(this) {
            binding.prgbar.isVisible = !it
            binding.prgbar.isVisible = it
        }
    }

    private fun textMessage(s: String) {
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show()
    }
}