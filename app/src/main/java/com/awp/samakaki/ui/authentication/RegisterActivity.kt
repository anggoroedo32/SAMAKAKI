package com.awp.samakaki.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivityRegisterBinding
import com.awp.samakaki.ui.SelamatDatangActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnRegister = binding.btnRegister
        btnRegister.setOnClickListener {
            val intent = Intent(this, SelamatDatangActivity::class.java)
            startActivity(intent)
        }

        val tvLogin = binding.txtViewLogin
        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}