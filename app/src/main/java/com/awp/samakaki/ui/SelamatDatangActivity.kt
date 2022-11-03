package com.awp.samakaki.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivitySelamatDatangBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.viewmodel.IsiProfilViewModel
import com.awp.samakaki.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log


@AndroidEntryPoint
class SelamatDatangActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelamatDatangBinding
    private val viewModelIsiProfile by viewModels<IsiProfilViewModel>()
    private lateinit var ivUploadImg: ImageView
    private val calendar = Calendar.getInstance()
    private var dateFormater: String? = null
    private val profileViewModel by viewModels<ProfileViewModel>()
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelamatDatangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dateFormater = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
        val btnIsiProfil = binding.btnIsiProfil

        btnIsiProfil.setOnClickListener{
            insertProfile()
            Toast.makeText(this, "Profil anda sudah dibuat", Toast.LENGTH_SHORT).show()
        }

        //Input Configuration
        inputConf()


        //Date Picker
        getDate()

        val token = SessionManager.getToken(this)
        val id = SessionManager.getIdUser(this)

        profileViewModel.findUser(token = "Bearer $token!!", id = id.toString())
        profileViewModel.findUser.observe(this) {
            when(it) {
                is BaseResponse.Loading -> showLoading()
                is BaseResponse.Success -> {
                    stopLoading()
                    it.data
                    Log.d("isi_data_biodata", "isinya : " + it.data?.data.toString())
                    if (it.data?.data?.biodata.isNullOrEmpty()) {

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
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

        ivUploadImg = binding.ivUploadImage
        val tvUploadImg = binding.tvUploadImage
        tvUploadImg.setOnClickListener{
            pickImageLauncher.launch("image/*")
        }
    }
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()){
            imageUri = it!!
            ivUploadImg.setImageURI(it)
            ivUploadImg.visibility = View.VISIBLE
            imageFile = uriToFile(imageUri!!, this)
        }

    private fun insertProfile() {
        val address = binding.etAddress.text.toString()
        val dob = binding.etBirthday.text.toString()
        var marriageStatus = binding.etStatus.text.toString()
        var status = binding.etPrivacy.text.toString().lowercase()

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

            status.isEmpty() -> {
                binding.etPrivacy.error = "Pilih privasi akunmu"
            }


            else -> {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                //Convert
                insertViewModel()
            }
        }
    }
    private fun insertViewModel(){
        val address = binding.etAddress.text.toString().toRequestBody("text/plain".toMediaType())
        val dob = binding.etBirthday.text.toString().toRequestBody("text/plain".toMediaType())
        var marriageStatus = binding.etStatus.text.toString().toRequestBody("text/plain".toMediaType())
        var status = binding.etPrivacy.text.toString().lowercase().toRequestBody("text/plain".toMediaType())
        var requestImage = imageFile?.asRequestBody("image/jpg".toMediaTypeOrNull())
        val avatar = requestImage?.let {
            MultipartBody.Part.createFormData(
                "avatar",
                imageFile?.name,
                it
            )
        }
        var tokenGet = SessionManager.getToken(this)
        Log.e("TAG", "insertViewModel: $address , $dob , ", )
        viewModelIsiProfile.createBiodata(
            "bearer $tokenGet",
            address,
            dob,
            marriageStatus,
            status,
            avatar!!
        )

        viewModelIsiProfile.createBiodataResponse.observe(this) {
            when(it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }
                is BaseResponse.Success -> {
                    stopLoading()
                    it.data
                }
                is BaseResponse.Error -> {
                    textMessage(it.msg.toString())
                }
            }
        }
    }

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("ExampleTime", ".jpg", storageDir)
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
                    dateFormater = SimpleDateFormat("yyyy-MM-dd").format(dateTime.time)
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