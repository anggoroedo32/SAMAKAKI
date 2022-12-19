package com.qatros.samakaki.ui.authentication

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.qatros.samakaki.R
import com.qatros.samakaki.databinding.ActivityRegisterBinding
import com.qatros.samakaki.helper.SessionManager
import com.qatros.samakaki.response.BaseResponse
import com.qatros.samakaki.viewmodel.AuthenticationViewModel
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
        SessionManager.saveInvitation(this, intent.getStringExtra("invit").toString())
        Log.d("isi_tokennnnn", "token $invitationToken")

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
                    binding.etName.error = getString(com.qatros.samakaki.R.string.err_empty_name)
                }
                email.isEmpty() -> {
                    binding.etEmail.error = getString(com.qatros.samakaki.R.string.er_empty_email)
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.etEmail.error = getString(com.qatros.samakaki.R.string.err_wrong_email_format)
                }
                password.isEmpty() -> {
                    binding.etPassword.error = getString(com.qatros.samakaki.R.string.err_empty_password)
                }
                confirmPassword.isEmpty() -> {
                    binding.etConfirmPassword.error = getString(com.qatros.samakaki.R.string.err_empty_confirmPassword)
                }
                password != confirmPassword -> {
                    binding.etConfirmPassword.error = getString(com.qatros.samakaki.R.string.err_password_did_not_match)
                    binding.etPassword.error = getString(com.qatros.samakaki.R.string.err_password_did_not_match)
                }
                else -> {

                    if (invitationToken.isNullOrEmpty()) {
                        authenticationViewModel.register(name = name, email = email, phone = phone, password = password)
                        authenticationViewModel.registerResponse.observe(this) {
                            it.getContentIfNotHandled()?.let {
                                when(it) {
                                    is BaseResponse.Success -> {
                                        showRegistrationSuccess()
                                    }
                                    is BaseResponse.Error -> {
                                        if (it.msg.toString().contains("invalid")){
                                            textMessage("Gunakan format email yang benar")
                                        } else if (it.msg.toString().contains("taken")) {
                                            textMessage("Email sudah terpakai")
                                        }

                                    }
                                }
                            }
                        }

                    } else if(!invitationToken.isNullOrEmpty()) {
                        authenticationViewModel.registerWithToken(name = name, email = email, phone = phone, password = password, token = invitationToken)
                        authenticationViewModel.registerWithTokenResponse.observe(this) {
                            it.getContentIfNotHandled()?.let {
                                when(it) {
                                    is BaseResponse.Success -> {
                                        showRegistrationSuccess()
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

    fun showRegistrationSuccess() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_registration_success)
        val btnToLogin = dialog.findViewById<Button>(R.id.btn_to_login)
        btnToLogin.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        dialog.setCancelable(true)
        dialog.show()
        val window: Window? = dialog.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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