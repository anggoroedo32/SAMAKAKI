package com.awp.samakaki.ui.menu_silsilah_keluarga

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.R
import com.awp.samakaki.databinding.FragmentFamilyBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.CurrentUser
import com.awp.samakaki.response.RelationItem
import com.awp.samakaki.utils.Graph
import com.awp.samakaki.viewmodel.FamilyTreeViewModel
import com.awp.samakaki.viewmodel.NotificationsViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.nikartm.support.ImageBadgeView
import java.util.*


@AndroidEntryPoint
abstract class SilsilahKeluargaFragment : Fragment() {

    private var _binding: FragmentFamilyBinding? = null
    private val binding get() = _binding!!
    protected lateinit var recyclerView: RecyclerView
//    protected lateinit var recyclerViewDataTop: RecyclerView
    protected lateinit var adapter: com.awp.samakaki.utils.AbstractGraphAdapter<NodeViewHolder>
//    protected lateinit var adapterDataTop: com.awp.samakaki.utils.AbstractGraphAdapter<NodeViewHolderDataTop>
    private var currentNode: com.awp.samakaki.utils.Node? = null
    private var nodeCount = 1
    private val familyTreeViewModel by viewModels<FamilyTreeViewModel>()
    private val notificationsViewModel by viewModels<NotificationsViewModel>()
    private var listRelation = listOf<RelationItem>()

    abstract fun createGraph(): com.awp.samakaki.utils.Graph
//    abstract fun createGraphDataTop(): com.awp.samakaki.utils.Graph
    abstract fun setLayoutManager()
//    abstract fun setLayoutManagerDataTop()
    abstract fun setEdgeDecoration()
//    abstract fun setEdgeDecorationDataTop()

    private var imageBadgeView: ImageBadgeView? = null
    var notificationsCount: Int = 0
    var hubungan = arrayOf<String?>(
        "father",
        "mother",
        "siblings",
        "child",
        "grandfather",
        "grandmother",
        "grandchild",
        "husband",
        "wife"
    )

    var dataRelationship: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamilyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val graph = createGraph()
//        val graphDataTop = createGraphDataTop()
        recyclerView = binding.rvFamilyTree
//        recyclerViewDataTop = binding.rvFamilyTreeDataTop
        setLayoutManager()
//        setLayoutManagerDataTop()
        setEdgeDecoration()
//        setEdgeDecorationDataTop()
//        setupGraphView(graph, listRelation)
//        setupGraphViewDataTop(graphDataTop)
        loadingState()

        val familyTree = binding.wrapFamilyTree
        val isiProfil = binding.wrapIsiProfil
        val token = context?.let { SessionManager.getToken(it) }

        familyTreeViewModel.findUserRelations("Bearer $token")
        familyTreeViewModel.findUserRelations.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {
                    val relationData = it.data?.data?.relation
                    Log.d("relation_data", "hasilnya $relationData")
                    if (relationData.isNullOrEmpty()) {
                        isiProfil.visibility = View.VISIBLE
                    } else {
                        familyTree.visibility = View.VISIBLE
                        setupGraphView(graph)
                    }
                }

                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }



        //Dropdown Relationship
        val relationshipDropdown = resources.getStringArray(R.array.relationship)
        val relationshipDropdownAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,relationshipDropdown)
        val autoCompleteRelationship = binding.etHubungan
        autoCompleteRelationship.setAdapter(relationshipDropdownAdapter)

        val toolbar = binding.toolbarHomepage
        toolbar.inflateMenu(R.menu.menu_home)
        initNotificationCounter()
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
//                R.id.notification -> findNavController().navigate(R.id.action_navigation_family_to_notificationsFragment)
                R.id.settings -> findNavController().navigate(R.id.action_navigation_family_to_settingsFragment)
            }
            true
        }

        val fab = binding.fab
        fab.setOnClickListener {
            familyTree.visibility = View.GONE
            isiProfil.visibility = View.VISIBLE
        }


        val btnAddProfile = binding.btnAddProfile
        btnAddProfile.setOnClickListener {
            val name = binding.etNama.text.trim().toString()
            val phone = binding.etNoTelp.text.trim().toString()
            val relationship = binding.etHubungan.text.toString()

            when{
                relationship == "Adek Pertama" -> dataRelationship = "adek_pertama"
                relationship == "Adek Kedua" -> dataRelationship = "adek_kedua"
                relationship == "Adek Ketiga" -> dataRelationship = "adek_ketiga"
                relationship == "Bapak" -> dataRelationship = "bapak"
                relationship == "Ibu" -> dataRelationship = "ibu"
                relationship == "Anak Pertama" -> dataRelationship = "anak_pertama"
                relationship == "Anak Kedua" -> dataRelationship = "anak_kedua"
                relationship == "Anak Ketiga" -> dataRelationship = "anak_ketiga"
                relationship == "Kakek Dari Bapak" -> dataRelationship = "kakek_dari_bapak"
                relationship == "Nenek Dari Bapak" -> dataRelationship = "nenek_dari_bapak"
                relationship == "Kakek Dari Ibu" -> dataRelationship = "kakek_dari_ibu"
                relationship == "Nenek Dari Ibu" -> dataRelationship = "nenek_dari_ibu"
            }


            when {
                name.isEmpty() -> {
                    binding.etNama.error = getString(R.string.err_empty_family_member)
                }
                phone.isEmpty() -> {
                    binding.etNoTelp.error = getString(R.string.err_empty_phone)
                }
                else -> {
                    familyTreeViewModel.createUserRelations("Bearer $token", name, dataRelationship.toString())
                    Log.d("isinya_data_relation" , dataRelationship.toString())
                    familyTreeViewModel.createUserRelations.observe(viewLifecycleOwner) {
                        when(it) {
                            is BaseResponse.Success -> {
                                it.data
                                isiProfil.visibility = View.GONE
                                familyTree.visibility = View.VISIBLE
                                val invitationToken = it.data?.data?.invitaionToken
                                showDialog(link = it.data?.data?.invitaionToken)
                                Log.d("isinya_token" , it.data?.data?.invitaionToken.toString())
                            }

                            is BaseResponse.Error -> (it.msg.toString())
                        }
                    }
                }

            }
        }

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

//        imageBadgeView?.setBadgeValue(notificationsCount)
//            ?.setMaxBadgeValue(99)
//            ?.setLimitBadgeValue(true)

        imageBadgeView?.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_family_to_notificationsFragment)
        }
    }

    private fun showDialog(link: String?) {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog)

        val btnClose = dialog.findViewById<ImageView>(R.id.close_btn)
        val edLink = dialog.findViewById<EditText>(R.id.ed_link)
        val btnCopy = dialog.findViewById<Button>(R.id.btn_copy)

        Log.d("link_edLinkValue", edLink.text.toString())


        edLink.setText("www.samakaki.com/$link")

        btnCopy.setOnClickListener {
            copyTextToClipboard(edLink.text.toString())
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()

    }

    private fun copyTextToClipboard(link: String) {
        Log.d("link_copy", link)
        val clipboardManager: ClipboardManager = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", link.toString())
        clipboardManager.setPrimaryClip(clipData)


        Toast.makeText(context, "Link telah di salin ke clipboard", Toast.LENGTH_LONG).show()
    }

    private fun setupGraphView(graph: Graph) {
        adapter = object : com.awp.samakaki.utils.AbstractGraphAdapter<NodeViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.node, parent, false)
                return NodeViewHolder(view)
            }

            override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
//                Glide.with(holder.itemView.context)
//                    .load(list.avatar)
//                    .centerInside()
//                    .into(holder.imgProfile)
//                holder.textView.text = Objects.requireNonNull(getNodeData(position)).toString()
                val relationUser = Objects.requireNonNull(getNodeData(position)).toString()
                val list: List<String> = listOf(*relationUser.split(",").toTypedArray())
                holder.textView.text = list.toString()
            }
        }.apply {
            this.submitGraph(graph)
            recyclerView.adapter = this
        }
    }

//    private fun setupGraphViewDataTop(graph: com.awp.samakaki.utils.Graph, list: CurrentUser) {
//        adapterDataTop = object : com.awp.samakaki.utils.AbstractGraphAdapter<NodeViewHolderDataTop>() {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolderDataTop {
//                val view = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.node, parent, false)
//                return NodeViewHolderDataTop(view)
//            }
//
//            override fun onBindViewHolder(holder: NodeViewHolderDataTop, position: Int) {
//                Glide.with(holder.itemView.context)
//                    .load(R.drawable.dummy_avatar)
//                    .centerInside()
//                    .into(holder.imgProfile)
//                holder.textView.text = list.name
//            }
//        }.apply {
//            this.submitGraph(graph)
//            recyclerViewDataTop.adapter = this
//        }
//    }


    protected inner class NodeViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.name)
        var imgProfile: ImageView = itemView.findViewById(R.id.img_profile)

//        init {
//            itemView.setOnClickListener {
//                currentNode = adapter.getNode(bindingAdapterPosition)
//                Snackbar.make(itemView, "Clicked on " + adapterDataTop.getNodeData(bindingAdapterPosition)?.toString(),
//                    Snackbar.LENGTH_SHORT).show()
//            }
//        }

    }

//    protected inner class NodeViewHolderDataTop internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var textView: TextView = itemView.findViewById(R.id.name)
//        var imgProfile: ImageView = itemView.findViewById(R.id.img_profile)
//
//    }

    protected val nodeText: String
        get() = "Node " + nodeCount++

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadingState(){
        familyTreeViewModel.loading.observe(viewLifecycleOwner){
            val progressBar =  binding.prgbar
            progressBar.isVisible = !it
            progressBar.isVisible = it
        }
    }

    protected fun textMessage(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }
}