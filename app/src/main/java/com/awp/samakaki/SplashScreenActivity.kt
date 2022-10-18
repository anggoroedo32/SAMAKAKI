package com.awp.samakaki

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.awp.samakaki.databinding.SplashscreenBinding
import com.awp.samakaki.ui.login.LoginFragment

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: SplashscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getStarted()
    }
    private fun getStarted() {
        binding.btnGetstarted.setOnClickListener {

            finish()
        }
    }
}