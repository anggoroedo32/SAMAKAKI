package com.qatros.samakaki.ui.menu_beranda

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qatros.samakaki.R
import com.qatros.samakaki.adapter.PostsAdapter
import com.qatros.samakaki.databinding.FragmentHomeBinding
import com.qatros.samakaki.helper.ConnectivityStatus
import com.qatros.samakaki.helper.SessionManager
import com.qatros.samakaki.response.BaseResponse
import com.qatros.samakaki.response.DataItem
import com.qatros.samakaki.viewmodel.NotificationsViewModel
import com.qatros.samakaki.viewmodel.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.nikartm.support.ImageBadgeView
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var ivUploadImg: ImageView
    private val viewModel by viewModels<PostsViewModel>()
    private var imageFile: File? = null
    private var _status: String = "public"
    private var imageBadgeView: ImageBadgeView? = null
    private val notificationsViewModel by viewModels<NotificationsViewModel>()


    var familyName = arrayOf<String?>(
        "Suharto Family",
        "Keluarga Cemara",
        "Arisan Keluarga"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkConnectivity()

        val toolbar = binding.toolbarHomepage
        toolbar.inflateMenu(R.menu.menu_home)
        initNotificationCounter()
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
//                R.id.notification -> findNavController().navigate(R.id.action_navigation_home_to_notificationsFragment)
                R.id.settings -> findNavController().navigate(R.id.action_navigation_home_to_settingsFragment)
            }
            true
        }

        //GetPosts
        getPosts()
        loadingState()

        val addMedia = binding.addMedia
        addMedia.setOnClickListener {
            val sortRecipesBottomSheet = BottomSheetFragment()
            sortRecipesBottomSheet.show(childFragmentManager,sortRecipesBottomSheet.tag)
        }

        val buttonPost:Button = binding.btnPost
        buttonPost.setOnClickListener(){
            buttonPost.isEnabled = false
            val caption = binding.edPost.text.toString()
            if (caption.isNotEmpty()) {
                _status.let { it1 -> insertViewModelPosts(it1) }
            } else {
                textMessage("Masukkan caption")
            }

        }

        val switchButton: SwitchCompat = binding.switchButton
        switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                _status = "private"
            } else {
                _status = "public"
            }
        }
        ivUploadImg = binding.ivUploadImage
        binding.addMedia.setOnClickListener{
            pickImageLauncher.launch("image/*")
        }
    }

    private fun checkConnectivity() {
        val connectivity = ConnectivityStatus(requireContext())
        connectivity.observe(viewLifecycleOwner, Observer {
                isConnected ->
            if(!isConnected){
                textMessageLong("Tidak ada koneksi internet")
            }
        })
    }

    private fun insertViewModelPosts(status: String){
        val caption = binding.edPost.text.toString().toRequestBody("text/plain".toMediaType())
        var isStatus = status.toRequestBody("text/plain".toMediaType())
        var requestImage = imageFile?.asRequestBody("image/jpg".toMediaTypeOrNull())
        var content = requestImage.let {
            it?.let { it1 ->
                MultipartBody.Part.createFormData(
                    "content",
                    imageFile?.name,
                    it1
                )
            }
        }

        var tokenGet = SessionManager.getToken(requireContext())
        viewModel.createPosts(
            "bearer $tokenGet",
            caption,
            isStatus,
            content
        )

        viewModel.createPostResponse.observe(viewLifecycleOwner){
            when(it){
                is BaseResponse.Success -> {
                    it.data
                    val buttonPost:Button = binding.btnPost
                    buttonPost.isEnabled = true
                    val destination = findNavController().currentDestination?.id
                    findNavController().popBackStack(destination!!,true)
                    findNavController().navigate(destination)
                }
                is BaseResponse.Error -> {
                    textMessage(it.msg.toString())
                }
            }
        }
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()){
            imageUri = it
            ivUploadImg.setImageURI(it)
            ivUploadImg.visibility = View.VISIBLE
            if (ivUploadImg.visibility == View.VISIBLE) {
                binding.edPost.setPadding(22)
            } else {
                binding.edPost.setPadding(32)
            }
            imageFile = imageUri?.let { it1 -> uriToFile(it1, requireContext()) }
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

    private fun initNotificationCounter() {
        val token = SessionManager.getToken(requireContext())
        imageBadgeView = view?.findViewById(R.id.notification_menu_icon)

        notificationsViewModel.getNotifications("Bearer $token")
        notificationsViewModel.getNotifications.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {
                    imageBadgeView?.badgeValue = it.data?.data?.unread?.size!!
                }

                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }

        imageBadgeView?.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_notificationsFragment)
        }
    }

    private fun observeData(){
        viewModel.listAllPosts.observe(viewLifecycleOwner) {
            when(it){
                is BaseResponse.Success -> {
                    it.data?.data?.let { it1 -> rvPosts(it1) }
                }
                is BaseResponse.Error -> {
                    textMessage(it.msg.toString())
                }
            }
        }
    }
    private fun rvPosts(list: List<DataItem>) {
        val recyclerViewPosts: RecyclerView = binding.rvPost
        recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            adapter = PostsAdapter(list)
        }
    }

    private fun getPosts(){
        var tokenGet = SessionManager.getToken(requireContext())
        viewModel.getAllPostsByFamily("bearer $tokenGet")
        observeData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("ExampleTime", ".jpg", storageDir)
    }

    private fun textMessage(s: String) {
        Toast.makeText(requireContext(),s, Toast.LENGTH_SHORT).show()
    }

    private fun textMessageLong(s: String) {
        Toast.makeText(requireContext(),s, Toast.LENGTH_LONG).show()
    }

    companion object{
        private val PICK_IMAGE = 100
        private var imageUri: Uri? = null
    }

    private fun loadingState(){
        viewModel.loading.observe(viewLifecycleOwner){
            binding.prgbar.isVisible = !it
            binding.prgbar.isVisible = it
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.prgbar.isVisible = !it
            binding.prgbar.isVisible = it
        }
    }
}
