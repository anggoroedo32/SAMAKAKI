package com.awp.samakaki.ui.menu_beranda

import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.R
import com.awp.samakaki.adapter.PostsAdapter
import com.awp.samakaki.databinding.FragmentHomeBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.PostItem
import com.awp.samakaki.viewmodel.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.nikartm.support.BadgePosition
import ru.nikartm.support.ImageBadgeView





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

    private var imageBadgeView: ImageBadgeView? = null


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
        initNotificationCounter()
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.notification -> Toast.makeText(context, "Clicked Notifications", Toast.LENGTH_SHORT).show()
                R.id.settings -> findNavController().navigate(R.id.action_navigation_home_to_settingsFragment)
            }
            true
        }



        viewModel.getAllPosts()
        viewModel.listAllPosts.observe(viewLifecycleOwner) {
            it.post?.let { it1 -> rvPosts(it1) }
        }


        val addMedia = binding.addMedia
        addMedia.setOnClickListener {
            val sortRecipesBottomSheet = BottomSheetFragment()
            sortRecipesBottomSheet.show(childFragmentManager,sortRecipesBottomSheet.tag)
        }

        val buttonPost:Button = binding.btnPost
        buttonPost.setOnClickListener(){
            createPosts()
        }
    }

    private fun initNotificationCounter() {
        imageBadgeView = view?.findViewById(R.id.notification_menu_icon)
        val value = 5
        imageBadgeView?.setBadgeValue(value)
            ?.setMaxBadgeValue(20)
            ?.setLimitBadgeValue(true)
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

    private fun createPosts(){
        val caption = binding.edPost.text.toString()
        val tokenGet = context?.let { SessionManager.getToken(it) }
        viewModel.observeIsCreate.observe(viewLifecycleOwner){
            it.let {
                if(it != null){
//                    viewModel.createPosts("Bearer $tokenGet", caption)
                    Toast.makeText(context, "Create Success", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
