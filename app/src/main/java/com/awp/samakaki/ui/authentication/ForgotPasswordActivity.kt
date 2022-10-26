package com.awp.samakaki.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnKonfirmasi = binding.btnKonfirmasi
        btnKonfirmasi.setOnClickListener {
            val intent = Intent(this, KodeVerifikasiActivity::class.java)
            startActivity(intent)
        }

    }

}