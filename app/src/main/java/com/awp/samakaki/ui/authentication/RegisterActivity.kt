package com.awp.samakaki.ui.authentication

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.awp.samakaki.databinding.ActivityRegisterBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.ui.MainActivity
import com.awp.samakaki.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel>()

    // creating a variable for our text view
    private lateinit var messageTV: TextView
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tvLogin = binding.txtViewLogin
        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // initializing our variable
        messageTV = binding.etTokenInvitation
        // getting the data from our intent in our uri.
        uri = intent.data

        // checking if the uri is null or not.
        if (uri != null) {
            // if the uri is not null then we are getting
            // the path segments and storing it in list.
            val parameters = uri!!.pathSegments

            // after that we are extracting string
            // from that parameters.
            val param = parameters[parameters.size - 1]

            // on below line we are setting that
            // string to our text view which
            // we got as params.
            messageTV.text = param
        }

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
                    authenticationViewModel.register(name = name, email = email, phone = phone, password = password)
                    authenticationViewModel.registerResponse.observe(this) {
                        it.getContentIfNotHandled()?.let {
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