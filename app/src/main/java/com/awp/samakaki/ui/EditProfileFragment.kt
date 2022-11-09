package com.awp.samakaki.ui

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.awp.samakaki.R
import com.awp.samakaki.databinding.FragmentEditprofileBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*


@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditprofileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    private var imageFile: File? = null
    private lateinit var ivUploadImg: ImageView
    private val calendar = Calendar.getInstance()
    private var dateFormater: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditprofileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Dropdown Status
        val statusDropdown = resources.getStringArray(R.array.status)
        val statusDropdownAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,statusDropdown)
        val autoCompleteStatus = binding.etStatus
        autoCompleteStatus.setAdapter(statusDropdownAdapter)

        //Dropdown Privacy
        val privacyDropdown = resources.getStringArray(R.array.privacy)
        val privacyDropdownAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,privacyDropdown)
        val autoCompletePrivacy = binding.etStatusakun
        autoCompletePrivacy.setAdapter(privacyDropdownAdapter)


        val btnBack = binding.btnBack

        btnBack.setOnClickListener{
            findNavController().popBackStack()
        }

        val name = binding.ETName
        val email = binding.ETEmail
        val dob = binding.ETTanggal
        val phone = binding.ETNoTlp
        val address = binding.ETLokasi
        val mariageStatus = binding.etStatus
        val status = binding.etStatusakun
        val avatar = binding.imgEditProfile


        val token = context?.let { SessionManager.getToken(it) }
        val id = SessionManager.getIdUser(requireContext())
        Log.d("id_dari_edit_profile", id.toString())
        profileViewModel.findUser(token = "Bearer $token!!", id = id.toString())
        profileViewModel.findUser.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {
                    it.data
                    name.setText(it.data?.data?.biodata?.name)
                    dob.setText(it.data?.data?.biodata?.dob)
                    phone.setText(it.data?.data?.biodata?.phone)
                    email.setText(it.data?.data?.biodata?.email)
                    address.setText(it.data?.data?.biodata?.address)
                    status.setText(it.data?.data?.biodata?.status)
                    mariageStatus.setText(it.data?.data?.biodata?.marriageStatus)

                    Glide.with(this)
                        .load(it.data?.data?.biodata?.avatar)
                        .centerInside()
                        .placeholder(R.drawable.dummy_avatar).error(R.drawable.dummy_avatar)
                        .into(avatar)

                }
                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }

        ivUploadImg = binding.imgEditProfile
        val btnUploadImg = binding.btnEdPhoto
        btnUploadImg.setOnClickListener{
            pickImageLauncher.launch("image/*")
        }


        val btnEdit = binding.btnSave
        btnEdit.setOnClickListener {
            edValidation()
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val fragmentManager = parentFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(
                    R.id.nav_host_fragment_activity_main,
                    ProfileFragment()
                )
                    .setReorderingAllowed(true)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

        })
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()){
            EditProfileFragment.imageUriEd = it!!
            ivUploadImg.setImageURI(it)
            ivUploadImg.visibility = View.VISIBLE
            imageFile = uriToFile(EditProfileFragment.imageUriEd!!, requireContext())
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

    private fun edValidation() {
        val name = binding.ETName.text.toString()
        val dob = binding.ETTanggal.text.toString()
        val phone = binding.ETNoTlp.text.toString()
        val marriageStatus = binding.etStatus.text.toString()

        when {
            name.isEmpty() -> binding.ETName.error = getString(R.string.err_empty_name)
            dob.isEmpty() -> binding.ETTanggal.error = getString(R.string.err_empty_dob)
            phone.isEmpty() -> binding.ETNoTlp.error = getString(R.string.err_empty_phone)
            marriageStatus.isEmpty() -> binding.etStatus.error = getString(R.string.err_empty_status)
            else -> {
                insertEditProfileData()
            }
        }
    }

    private fun insertEditProfileData() {
        val name = binding.ETName.text.toString().toRequestBody("text/plain".toMediaType())
        val email = binding.ETEmail.text.toString().toRequestBody("text/plain".toMediaType())
        val address = binding.ETLokasi.text.toString().toRequestBody("text/plain".toMediaType())
        val dob = binding.ETTanggal.text.toString().toRequestBody("text/plain".toMediaType())
        val phone = binding.ETNoTlp.text.toString().toRequestBody("text/plain".toMediaType())
        val status = binding.etStatus.text.toString().toRequestBody("text/plain".toMediaType())
        val marriageStatus = binding.etStatus.text.toString().toRequestBody("text/plain".toMediaType())
        var requestImage = imageFile?.asRequestBody("image/jpg".toMediaTypeOrNull())
        val avatar = requestImage?.let {
            MultipartBody.Part.createFormData(
                "avatar",
                imageFile?.name,
                it
            )
        }


        // TODO: ngenteni rampung sing ngedit layout sik, layout e durung lengkap dan belum sesuai data dari BE

        val token = SessionManager.getToken(requireContext())
        val id = SessionManager.getIdUser(requireContext())
        profileViewModel.editProfile("Bearer $token", name, email, dob, phone, address, status, marriageStatus, avatar)
        profileViewModel.editProfile.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {
                    it.data
                    textMessage("Profile anda sudah di perbarui")
                }

                is BaseResponse.Error -> {
                    textMessage(it.msg.toString())
                }
            }
        }


    }

    private fun textMessage(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }

    companion object{
        private val PICK_IMAGE = 100
        private var imageUriEd: Uri? = null
    }


}