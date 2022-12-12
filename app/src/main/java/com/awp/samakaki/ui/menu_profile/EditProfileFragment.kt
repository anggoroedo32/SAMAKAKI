package com.awp.samakaki.ui.menu_profile

import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.awp.samakaki.R
import com.awp.samakaki.databinding.FragmentEditprofileBinding
import com.awp.samakaki.helper.ConnectivityStatus
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
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
import java.text.DateFormat
import java.text.SimpleDateFormat
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
        checkConnectivity()

        binding.ETTanggal.setOnClickListener {
            getDate()
        }
        loadingState()

        val btnBack = binding.btnBack
        btnBack.setOnClickListener{
            if (!findNavController().popBackStack()) findNavController().navigate(R.id.navigation_home)
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
                    if (it.data?.data?.biodata?.dob?.isNotEmpty() == true) {
                        formatDate(it.data?.data?.biodata?.dob.toString())
                    } else if (it.data?.data?.biodata?.dob?.isNullOrBlank() == true) {
                        binding.ETTanggal.setText(it.data?.data?.biodata?.dob?.toString())
                    }
                    phone.setText(it.data?.data?.biodata?.phone)
                    email.setText(it.data?.data?.biodata?.email)
                    address.setText(it.data?.data?.biodata?.address)

                    //Dropdown Status
                    val statusDropdown = resources.getStringArray(R.array.status)
                    val statusDropdownAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,statusDropdown)
                    val autoCompleteStatus = binding.etStatus
                    autoCompleteStatus.setAdapter(statusDropdownAdapter)
                    val selectionMarriageStatus = it.data?.data?.biodata?.marriageStatus
                    val spinnerPositionMarriageStatus: Int = statusDropdownAdapter.getPosition(selectionMarriageStatus)
                    autoCompleteStatus.setSelection(spinnerPositionMarriageStatus)


                    //Dropdown Privacy
                    val privacyDropdown = resources.getStringArray(R.array.privacy)
                    val privacyDropdownAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,privacyDropdown)
                    val autoCompletePrivacy = binding.etStatusakun
                    autoCompletePrivacy.setAdapter(privacyDropdownAdapter)
                    val selection = it.data?.data?.biodata?.status
                    val spinnerPosition: Int = privacyDropdownAdapter.getPosition(selection)
                    autoCompletePrivacy.setSelection(spinnerPosition)

                    Picasso.get()
                        .load(it.data?.data?.biodata?.avatar)
                        .fit()
                        .centerInside()
                        .error(R.drawable.dummy_avatar)
                        .into(avatar)

                }
                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }

        ivUploadImg = binding.imgEditProfile
        val btnUploadImg = binding.btnEdPhoto
        btnUploadImg.setOnClickListener{
            pickImageLauncher.launch(arrayOf("image/*"))
        }


        val btnEdit = binding.btnSave
        btnEdit.setOnClickListener {
            edValidation()
        }

    }


    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()){
            if (it != null) {
                val resolver = requireActivity().contentResolver
                resolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                imageUriEd = it
                ivUploadImg.visibility = View.VISIBLE
            }
            ivUploadImg.setImageURI(it)
            imageFile = imageUriEd?.let { it1 -> uriToFile(it1, requireContext()) }
//            imageFile = uriToFile(imageUri, this)
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
        Log.d("TAG", "edValidation: $dob")

        when {
            name.isEmpty() -> binding.ETName.error = getString(R.string.err_empty_name)
            dob.isEmpty() -> binding.ETTanggal.error = getString(R.string.err_empty_dob)
            phone.isEmpty() -> binding.ETNoTlp.error = getString(R.string.err_empty_phone)
            else -> {
                insertEditProfileData(dob)
            }
        }
    }

    private fun insertEditProfileData(dob: String) {
        val name = binding.ETName.text.toString().toRequestBody("text/plain".toMediaType())
        val email = binding.ETEmail.text.toString().toRequestBody("text/plain".toMediaType())
        val address = binding.ETLokasi.text.toString().toRequestBody("text/plain".toMediaType())
        val dob = dob.toRequestBody("text/plain".toMediaType())
        val phone = binding.ETNoTlp.text.toString().toRequestBody("text/plain".toMediaType())
        val status = binding.etStatusakun.selectedItem.toString().toRequestBody("text/plain".toMediaType())
        val marriageStatus = binding.etStatus.selectedItem.toString().toRequestBody("text/plain".toMediaType())
        var requestImage = imageFile?.asRequestBody("image/jpg".toMediaTypeOrNull())
        val avatar = requestImage?.let {
            MultipartBody.Part.createFormData(
                "avatar",
                imageFile?.name,
                it
            )
        }


        val token = SessionManager.getToken(requireContext())
        val id = SessionManager.getIdUser(requireContext())
        profileViewModel.editProfile("Bearer $token", id, name, email, phone, address , dob, marriageStatus, status, avatar)
        profileViewModel.editProfile.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {
                    it.data
                    findNavController().popBackStack()
                }
                is BaseResponse.Error -> {
                    textMessage(it.msg.toString())
                }
            }
        }


    }

    private fun checkConnectivity() {
        val connectivity = ConnectivityStatus(requireContext())
        connectivity.observe(viewLifecycleOwner) {
                isConnected ->
            if(!isConnected){
                textMessageLong("Tidak ada koneksi internet")
            }
        }
    }

    private fun textMessageLong(s: String) {
        Toast.makeText(requireContext(),s, Toast.LENGTH_LONG).show()
    }

    private fun textMessage(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }

    companion object{
        private val PICK_IMAGE = 100
        private var imageUriEd: Uri? = null
    }

    private fun formatDate(inputDate: String) {
        var inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        var dateFormater: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        var date: Date = inputFormat.parse(inputDate)
        var outputDate: String = dateFormater.format(date)
        binding.ETTanggal.setText(outputDate)
    }

    private fun getDate(){
//        var day = calendar.get(Calendar.DAY_OF_MONTH)
//        var month = calendar.get(Calendar.MONTH)
//        var year = calendar.get(Calendar.YEAR)
//        val dateTime = Calendar.getInstance()
//        context?.let { it1 ->
//            DatePickerDialog(
//                it1,
//                { view, year, month, day ->
//                    dateTime.set(year,month,day)
//                    dateFormater = SimpleDateFormat("dd MMMM yyyy").format(dateTime.time)
//                    binding.ETTanggal.setText(dateFormater)
//                    Log.d("TAG", "getDate: $dateFormater")
//                },
//                year,month,day
//            ).show()
//        }

        var c = Calendar.getInstance()
        var cDay = c.get(Calendar.DAY_OF_MONTH)
        var cMonth = c.get(Calendar.MONTH)
        var cYear = c.get(Calendar.YEAR)

        val calenderDialog = DatePickerDialog(requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                cDay = dayOfMonth
                cMonth = month
                cYear = year
                val bornDate = binding.ETTanggal
                bornDate.setText("$cDay-${cMonth+1}-$cYear")

            }, cYear, cMonth, cDay)
        calenderDialog.show()

    }

    private fun loadingState(){
        profileViewModel.loading.observe(viewLifecycleOwner){
            binding.prgbar.isVisible = !it
            binding.prgbar.isVisible = it
        }

        profileViewModel.loading.observe(viewLifecycleOwner) {
            binding.prgbar.isVisible = !it
            binding.prgbar.isVisible = it
        }
    }


}