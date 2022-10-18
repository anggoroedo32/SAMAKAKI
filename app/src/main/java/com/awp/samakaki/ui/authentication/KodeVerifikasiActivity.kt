package com.awp.samakaki.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivityKodeVerifikasiBinding

class KodeVerifikasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKodeVerifikasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKodeVerifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnEmailVerification = binding.btnEmailVerification
        btnEmailVerification.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

    }
}