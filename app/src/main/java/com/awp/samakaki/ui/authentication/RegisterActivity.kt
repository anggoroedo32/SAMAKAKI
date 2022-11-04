package com.awp.samakaki.ui.authentication

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.awp.samakaki.databinding.ActivityRegisterBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.ui.MainActivity
import com.awp.samakaki.ui.SelamatDatangActivity
import com.awp.samakaki.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel>()
    private lateinit var tokenInvitation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        loadingState()
        val tvLogin = binding.txtViewLogin
        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val invitationToken = intent.getStringExtra("invit")
        val ss:String = intent.getStringExtra("invit").toString()

        tokenInvitation = binding.etTokenInvitation
        tokenInvitation.text = invitationToken

        val btnRegister = binding.btnRegister
        btnRegister.setOnClickListener {
            val name = binding.etName.text.trim().toString()
            val email = binding.etEmail.text.trim().toString()
            val phone = binding.etNoTelp.text.trim().toString()
            val password = binding.etPassword.text.trim().toString()
            val confirmPassword = binding.etConfirmPassword.text.trim().toString()


            when{
                name.isEmpty() -> {
                    binding.etName.error = getString(com.awp.samakaki.R.string.err_empty_name)
                }
                email.isEmpty() -> {
                    binding.etEmail.error = getString(com.awp.samakaki.R.string.er_empty_email)
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.etEmail.error = getString(com.awp.samakaki.R.string.err_wrong_email_format)
                }
                password.isEmpty() -> {
                    binding.etPassword.error = getString(com.awp.samakaki.R.string.err_empty_password)
                }
                confirmPassword.isEmpty() -> {
                    binding.etConfirmPassword.error = getString(com.awp.samakaki.R.string.err_empty_confirmPassword)
                }
                password != confirmPassword -> {
                    binding.etConfirmPassword.error = getString(com.awp.samakaki.R.string.err_password_did_not_match)
                    binding.etPassword.error = getString(com.awp.samakaki.R.string.err_password_did_not_match)
                }
                else -> {

                    if (invitationToken.isNullOrEmpty()) {
                        authenticationViewModel.register(name = name, email = email, phone = phone, password = password)
                        authenticationViewModel.registerResponse.observe(this) {
                            it.getContentIfNotHandled()?.let {
                                when(it) {
                                    is BaseResponse.Success -> {
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        textMessage("Akun Anda Sudah Dibuat")
                                    }
                                    is BaseResponse.Error -> textMessage(it.msg.toString())
                                }
                            }
                        }

                    } else if(!invitationToken.isNullOrEmpty()) {
                        authenticationViewModel.registerWithToken(name = name, email = email, phone = phone, password = password, token = invitationToken)
                        authenticationViewModel.registerWithTokenResponse.observe(this) {
                            it.getContentIfNotHandled()?.let {
                                when(it) {
                                    is BaseResponse.Success -> {
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        textMessage("Akun Anda Sudah Dibuat")
                                    }
                                    is BaseResponse.Error -> textMessage(it.msg.toString())
                                }
                            }
                        }
                    }

                }
            }

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