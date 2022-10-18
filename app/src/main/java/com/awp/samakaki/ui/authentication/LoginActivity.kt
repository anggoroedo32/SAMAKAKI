package com.awp.samakaki.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivityLoginBinding
import com.awp.samakaki.ui.SelamatDatangActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnLogin = binding.btnLogin
        btnLogin.setOnClickListener {
            val intent = Intent(this, SelamatDatangActivity::class.java)
            startActivity(intent)
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
}