package com.awp.samakaki.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivityRegisterBinding
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.RegisterResponse
import com.awp.samakaki.ui.SelamatDatangActivity
import com.awp.samakaki.viewmodel.AuthenticationViewModel
import com.awp.samakaki.viewmodel.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tvLogin = binding.txtViewLogin
        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val btnRegister = binding.btnRegister
        btnRegister.setOnClickListener {
            val name = binding.etName.text.trim().toString()
            val email = binding.etEmail.text.trim().toString()
            val phone = binding.etNoTelp.text.trim().toString()
            val password = binding.etPassword.text.trim().toString()
            val confirmPassword = binding.etConfirmPassword.text.trim().toString()


//            if (name.isEmpty()){
//                binding.etName.error = getString(R.string.err_empty_name)
//            }
//            if (email.isEmpty()) {
//                binding.etEmail.error = getString(R.string.er_empty_email)
//            }
//            if (phone.isEmpty()) {
//                binding.etNoTelp.error = getString(R.string.err_empty_phone)
//            }
//            if (password.isEmpty()){
//                binding.etPassword.error = getString(R.string.err_empty_password)
//            }
//            if (confirmPassword.isEmpty()) {
//                binding.etConfirmPassword.error = getString(R.string.err_empty_confirmPassword)
//            }


            when{
                name.isEmpty() -> {
                    binding.etName.error = getString(R.string.err_empty_name)
                }
                email.isEmpty() -> {
                    binding.etEmail.error = getString(R.string.er_empty_email)
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.etEmail.error = getString(R.string.err_wrong_email_format)
                }
                password.isEmpty() -> {
                    binding.etPassword.error = getString(R.string.err_empty_password)
                }
                confirmPassword.isEmpty() -> {
                    binding.etConfirmPassword.error = getString(R.string.err_empty_confirmPassword)
                }
                password != confirmPassword -> {
                    binding.etConfirmPassword.error = getString(R.string.err_password_did_not_match)
                    binding.etPassword.error = getString(R.string.err_password_did_not_match)
                }
                else -> {
                    authenticationViewModel.register(name = name, email = email, phone = phone, password = password)
                    authenticationViewModel.registerResponse.observe(this) {
                        when(it) {
                            is BaseResponse.Loading -> {
                                showLoading()
                            }
                            is BaseResponse.Success -> {
                                stopLoading()
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