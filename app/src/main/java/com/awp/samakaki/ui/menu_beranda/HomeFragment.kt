package com.awp.samakaki.ui.menu_beranda

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.R
import com.awp.samakaki.adapter.PostsAdapter
import com.awp.samakaki.databinding.FragmentHomeBinding
import com.awp.samakaki.response.PostItem
import com.awp.samakaki.viewmodel.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<PostsViewModel>()
    private lateinit var postsAdapter: PostsAdapter

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

        val toolbar = binding.toolbarHomepage
        toolbar.inflateMenu(R.menu.menu_home)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.notification -> Toast.makeText(context, "Clicked Notifications", Toast.LENGTH_SHORT).show()
                R.id.settings -> findNavController().navigate(R.id.action_navigation_home_to_settingsFragment)
            }
            true
        }

//        val spin = binding.searchFamily
//        spin.onItemSelectedListener = this
//
//        val ad: ArrayAdapter<*> = ArrayAdapter(requireContext(), R.layout.spinner_item, familyName)
//        ad.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
//        spin.adapter = ad


        viewModel.getAllPosts()
        viewModel.listAllPosts.observe(viewLifecycleOwner) {
            it.post?.let { it1 -> rvPosts(it1) }
        }


        val addMedia = binding.addMedia
        addMedia.setOnClickListener {
            val sortRecipesBottomSheet = BottomSheetFragment()
            sortRecipesBottomSheet.show(childFragmentManager,sortRecipesBottomSheet.tag)
        }


    }

    private fun rvPosts(list: List<PostItem>) {
        val recyclerViewPosts: RecyclerView = binding.rvPost
        postsAdapter = PostsAdapter(list)
        recyclerViewPosts.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = postsAdapter
        }
    }



//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        Toast.makeText(context,
//            familyName[position],
//            Toast.LENGTH_LONG)
//            .show()
//    }
//
//    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}