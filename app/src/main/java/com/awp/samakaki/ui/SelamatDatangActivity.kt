package com.awp.samakaki.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivitySelamatDatangBinding
import com.awp.samakaki.viewmodel.IsiProfilViewModel
import java.text.SimpleDateFormat
import java.util.*

class SelamatDatangActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelamatDatangBinding
    private val viewModelIsiProfile by viewModels<IsiProfilViewModel>()
    private lateinit var ivUploadImg: ImageView
    private val calendar = Calendar.getInstance()
    private var dateFormater: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelamatDatangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dateFormater = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)

        //Input Configuration
        inputConf()

        //Intent and post isi profile
        insertProfile()

        //Date Picker
        getDate()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
         imageUri = data?.data
            ivUploadImg.visibility = View.VISIBLE
         ivUploadImg.setImageURI(imageUri)
      }
    }

    private fun inputConf(){
        //Dropdown Status
        val statusDropdown = resources.getStringArray(R.array.status)
        val statusDropdownAdapter = ArrayAdapter(this,R.layout.dropdown_item,statusDropdown)
        val autoCompleteStatus = binding.etStatus
        autoCompleteStatus.setAdapter(statusDropdownAdapter)

        //Dropdown Privacy
        val privacyDropdown = resources.getStringArray(R.array.privacy)
        val privacyDropdownAdapter = ArrayAdapter(this,R.layout.dropdown_item,privacyDropdown)
        val autoCompletePrivacy = binding.etPrivacy
        autoCompletePrivacy.setAdapter(privacyDropdownAdapter)

        //Open Galery
        ivUploadImg = binding.ivUploadImage
        val tvUploadImg = binding.tvUploadImage
        tvUploadImg.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
        }
    }

    private fun insertProfile() {
        val btnIsiProfil = binding.btnIsiProfil
        btnIsiProfil.setOnClickListener {
            val address = binding.etAddress.text.toString()
            val dob = binding.etBirthday.text.toString()
            var marriageStatus = binding.etStatus.text.toString()
            var status = binding.etPrivacy.text.toString().lowercase()
            lateinit var newMarriageStatus: String

            when {
                address.isEmpty() -> {
                    binding.etAddress.error = "Isi alamatmu"
                }
                dob.isEmpty() -> {
                    binding.etBirthday.error = "Isi tanggal lahirmu"
                }
                marriageStatus.isEmpty() -> {
                    binding.etStatus.error = "Pilih status pernikahanmu"
                }
                marriageStatus.contains("Belum Menikah") -> {
                    newMarriageStatus = "single"
                }
                marriageStatus.contains("Menikah") -> {
                    newMarriageStatus = "married"
                }
                marriageStatus.contains("Cerai") -> {
                    newMarriageStatus = "divorced"
                }
                status.isEmpty() -> {
                    binding.etPrivacy.error = "Pilih privasi akunmu"
                }

                else -> {
                    viewModelIsiProfile.createBiodata(address, dob, newMarriageStatus, status)
                    viewModelIsiProfile.createBiodataResponse.observe(this) {
                        it.let {
                            if (it != null){
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                Toast.makeText(this, "Success Insert", Toast.LENGTH_SHORT).show()
                                Log.d("it_data", it.toString())
                            } else {
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                                Log.d("it_data_failure", it.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getDate(){
        binding.etBirthday.setOnClickListener {
            var day = calendar.get(Calendar.DAY_OF_MONTH)
            var month = calendar.get(Calendar.MONTH)
            var year = calendar.get(Calendar.YEAR)
            val dateTime = Calendar.getInstance()
            DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    dateTime.set(year,month,day)
                    dateFormater = SimpleDateFormat("dd/MM/yyyy").format(dateTime.time)
                    binding.etBirthday.setText(dateFormater)
                },
                year,
                month,
                day
            ).show()
        }
    }

    companion object{
        private val PICK_IMAGE = 100
        private var imageUri: Uri? = null
    }
}