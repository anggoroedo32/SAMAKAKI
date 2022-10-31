package com.awp.samakaki.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivitySelamatDatangBinding

class SelamatDatangActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelamatDatangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelamatDatangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusDropdown = resources.getStringArray(R.array.status)
        val statusDropdownAdapter = ArrayAdapter(this,R.layout.dropdown_item,statusDropdown)
        val autoCompleteStatus = binding.etStatus
        autoCompleteStatus.setAdapter(statusDropdownAdapter)

        val privacyDropdown = resources.getStringArray(R.array.privacy)
        val privacyDropdownAdapter = ArrayAdapter(this,R.layout.dropdown_item,privacyDropdown)
        val autoCompletePrivacy = binding.etPrivacy
        autoCompletePrivacy.setAdapter(privacyDropdownAdapter)

        val btnIsiProfil = binding.btnIsiProfil
        btnIsiProfil.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}