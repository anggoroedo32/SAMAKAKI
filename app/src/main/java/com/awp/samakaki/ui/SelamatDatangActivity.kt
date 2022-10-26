package com.awp.samakaki.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivitySelamatDatangBinding

class SelamatDatangActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelamatDatangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelamatDatangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnIsiProfil = binding.btnIsiProfil
        btnIsiProfil.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}