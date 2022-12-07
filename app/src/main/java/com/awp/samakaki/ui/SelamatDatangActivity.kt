package com.awp.samakaki.ui

import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.awp.samakaki.R
import com.awp.samakaki.databinding.ActivitySelamatDatangBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.viewmodel.IsiProfilViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class SelamatDatangActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelamatDatangBinding
    private val viewModelIsiProfile by viewModels<IsiProfilViewModel>()
    private lateinit var ivUploadImg: ImageView
    private val calendar = Calendar.getInstance()
    private var dateFormater: String? = null
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelamatDatangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingState()

        val btnIsiProfil = binding.btnIsiProfil

        binding.etBirthday.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                binding.etBirthday.error = null
            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
            override fun afterTextChanged(arg0: Editable) {}
        })

        btnIsiProfil.setOnClickListener{
            insertProfile()
        }

        //Input Configuration
        inputConf()

        //Date Picker
        dateFormater = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
        getDate()
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
            pickImageLauncher.launch(arrayOf("image/*"))
        }
    }
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()){
            if (it != null) {
                contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            if (it != null) {
                imageUri = it
                ivUploadImg.visibility = View.VISIBLE
            }
            ivUploadImg.setImageURI(it)
            imageFile = imageUri?.let { it1 -> uriToFile(it1, this) }
//            imageFile = uriToFile(imageUri, this)
        }

    private fun insertProfile() {
        val address = binding.etAddress.text.toString()
        val dob = binding.etBirthday.text.toString()
        var marriageStatus = binding.etStatus
        var status = binding.etPrivacy

        val img = binding.ivUploadImage

        when {
            address.isEmpty() -> {
                binding.etAddress.error = "Isi alamatmu"
            }
            dob.isEmpty() -> {
                binding.etBirthday.error = "Isi tanggal lahirmu"
            }

            marriageStatus.isEmpty() -> {
                (marriageStatus.selectedView as TextView).error = "Pilih status pernikahanmu"
            }

            status.isEmpty() -> {
                (status.selectedView as TextView).error = "Pilih privasi akunmu"
            }
//            status.isEmpty() -> {
//                binding.etPrivacy.error = "Pilih privasi akunmu"
//            }

            img.visibility == View.GONE -> {
                textMessage("Masukkan avatar anda")
            }


            else -> {
                var intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                //Convert
                insertViewModel()
            }
        }
    }


    private fun insertViewModel(){
        val address = binding.etAddress.text.toString().toRequestBody("text/plain".toMediaType())
        val dob = binding.etBirthday.text.toString().toRequestBody("text/plain".toMediaType())
        var marriageStatus = binding.etStatus.selectedItem.toString().toRequestBody("text/plain".toMediaType())
        var status = binding.etPrivacy.selectedItem.toString().lowercase().toRequestBody("text/plain".toMediaType())
        var requestImage = imageFile?.asRequestBody("image/jpg".toMediaTypeOrNull())
        val avatar = requestImage?.let {
            MultipartBody.Part.createFormData(
                "avatar",
                imageFile?.name,
                it
            )
        }
        var tokenGet = SessionManager.getToken(this)
        Log.e("TAG", "insertViewModel: $address , $dob , ")
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
                is BaseResponse.Success -> {
                    stopLoading()
                    it.data
                    Toast.makeText(this, "Profil anda sudah dibuat", Toast.LENGTH_SHORT).show()
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
//        val inputStream = selectedImg?.let { contentResolver.openInputStream(it) } as InputStream
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

    private fun loadingState(){
        viewModelIsiProfile.loading.observe(this){
            binding.prgbar.isVisible = !it
            binding.prgbar.isVisible = it
        }
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
                R.style.DatePickerTheme,
                { view, year, month, day ->
                    dateTime.set(year,month,day)
                    dateFormater = SimpleDateFormat("dd MMMM yyyy").format(dateTime.time)
                    binding.etBirthday.setText(dateFormater)
                },
                year,month,day
            ).show()
        }
    }

    companion object{
        private val PICK_IMAGE = 100
        private var imageUri: Uri? = null
    }
}