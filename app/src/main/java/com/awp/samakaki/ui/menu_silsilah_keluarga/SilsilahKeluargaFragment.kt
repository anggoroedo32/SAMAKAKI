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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.R
import com.awp.samakaki.databinding.FragmentFamilyBinding
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.request.CreateFamilyTreeRequest
import com.awp.samakaki.request.CreateRelationsRequest
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.viewmodel.FamilyTreeViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
abstract class SilsilahKeluargaFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentFamilyBinding? = null
    private val binding get() = _binding!!
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var recyclerViewDataTop: RecyclerView
    protected lateinit var adapter: com.awp.samakaki.utils.AbstractGraphAdapter<NodeViewHolder>
    protected lateinit var adapterDataTop: com.awp.samakaki.utils.AbstractGraphAdapter<NodeViewHolderDataTop>
    private var currentNode: com.awp.samakaki.utils.Node? = null
    private var nodeCount = 1
    private val familyTreeViewModel by viewModels<FamilyTreeViewModel>()

    abstract fun createGraph(): com.awp.samakaki.utils.Graph
    abstract fun createGraphDataTop(): com.awp.samakaki.utils.Graph
    abstract fun setLayoutManager()
    abstract fun setLayoutManagerDataTop()
    abstract fun setEdgeDecoration()
    abstract fun setEdgeDecorationDataTop()

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
        val graphDataTop = createGraphDataTop()
        recyclerView = binding.rvFamilyTree
        recyclerViewDataTop = binding.rvFamilyTreeDataTop
        setLayoutManager()
        setLayoutManagerDataTop()
        setEdgeDecoration()
        setEdgeDecorationDataTop()
        setupGraphView(graph)
        setupGraphViewDataTop(graphDataTop)


        val toolbar = binding.toolbarHomepage
        toolbar.inflateMenu(R.menu.menu_home)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.notification -> Toast.makeText(context, "Clicked Notifications", Toast.LENGTH_SHORT).show()
                R.id.settings -> Toast.makeText(context, "Clicked Settings", Toast.LENGTH_SHORT).show()
            }
            true
        }

        val familyTree = binding.wrapFamilyTree
        val isiProfil = binding.wrapIsiProfil
        val token = context?.let { SessionManager.getToken(it) }
        familyTreeViewModel.findUserRelations("Bearer $token")
        familyTreeViewModel.findUserRelations.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Loading -> showLoading()

                is BaseResponse.Success -> {
                    stopLoading()
                    val relationData = it.data?.data?.relation
                    Log.d("relation_data", "hasilnya $relationData")
                    if (relationData.isNullOrEmpty()) {
                        isiProfil.visibility = View.VISIBLE
                    } else {
                        familyTree.visibility = View.VISIBLE
//                        isiProfil.visibility = View.VISIBLE
                    }
                }

                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }


        val btnAddProfile = binding.btnAddProfile
        btnAddProfile.setOnClickListener {
            val familyName = binding.etFamilyName.text.trim().toString()
            val familyMember = binding.etNama.text.trim().toString()
            val phone = binding.etNoTelp.text.trim().toString()
            val relationship = binding.etHubungan
            val dataRelationship = relationship.selectedItem.toString()

            when {
                familyName.isEmpty() -> {
                    binding.etFamilyName.error = getString(R.string.err_empty_family_name)
                }
                familyMember.isEmpty() -> {
                    binding.etNama.error = getString(R.string.err_empty_family_member)
                }
                phone.isEmpty() -> {
                    binding.etNoTelp.error = getString(R.string.err_empty_phone)
                }
                else -> {

                    val createFamilyUserRequest = CreateFamilyTreeRequest(
                        name = familyName
                    )

                    familyTreeViewModel.createFamilyTree("Bearer $token", createFamilyUserRequest )
                    familyTreeViewModel.createFamilyTree.observe(viewLifecycleOwner) { it ->
                        when(it) {
                            is BaseResponse.Loading -> showLoading()

                            is BaseResponse.Success -> {
                                stopLoading()
                                it.data
                                val idFamilyTree = it.data?.data?.id

                                val createUserRelationsRequest = CreateRelationsRequest(
                                    name = familyName,
                                    relation_name = dataRelationship,
                                    family_tree_id = idFamilyTree.toString()
                                )

                                familyTreeViewModel.createUserRelations("Bearer $token", familyName, dataRelationship, idFamilyTree.toString())
                                familyTreeViewModel.createUserRelations.observe(viewLifecycleOwner) {
                                    when(it) {
                                        is BaseResponse.Loading -> showLoading()

                                        is BaseResponse.Success -> {
                                            stopLoading()
                                            it.data
                                            isiProfil.visibility = View.GONE
                                            familyTree.visibility = View.VISIBLE
                                            val invitationToken = it.data?.dataCreate?.invitaionToken
                                            showDialog(link = it.data?.dataCreate?.invitaionToken)
                                            Log.d("isinya_token" , it.data?.dataCreate?.invitaionToken.toString())
                                        }

                                        is BaseResponse.Error -> (it.msg.toString())
                                    }
                                }

                            }

                            is BaseResponse.Error -> textMessage(it.msg.toString())
                        }
                    }
                }
            }
        }

        val spin = binding.etHubungan
        spin.onItemSelectedListener = this

        val ad: ArrayAdapter<*> = ArrayAdapter(requireContext(), R.layout.spinner_item, hubungan)
        ad.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        spin.adapter = ad

    }

    private fun showDialog(link: String?) {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog)

        val btnClose = dialog.findViewById<ImageView>(R.id.close_btn)
        val edLink = dialog.findViewById<EditText>(R.id.ed_link)
        val btnCopy = dialog.findViewById<Button>(R.id.btn_copy)

        Log.d("link_edLinkValue", edLink.text.toString())


        edLink.setText(link)

        btnCopy.setOnClickListener {
            copyTextToClipboard(edLink.text.toString())
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.show()

    }

    private fun copyTextToClipboard(link: String) {
        Log.d("link_copy", link)
        val clipboardManager: ClipboardManager = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", link.toString())
        clipboardManager.setPrimaryClip(clipData)


        Toast.makeText(context, "Link telah di salin ke clipboard", Toast.LENGTH_LONG).show()
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun setupGraphView(graph: com.awp.samakaki.utils.Graph) {
        adapter = object : com.awp.samakaki.utils.AbstractGraphAdapter<NodeViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.node, parent, false)
                return NodeViewHolder(view)
            }

            override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
                Glide.with(holder.itemView.context)
                    .load(R.drawable.dummy_avatar)
                    .centerInside()
                    .into(holder.imgProfile)
                holder.textView.text = Objects.requireNonNull(getNodeData(position)).toString()
            }
        }.apply {
            this.submitGraph(graph)
            recyclerView.adapter = this
        }
    }

    private fun setupGraphViewDataTop(graph: com.awp.samakaki.utils.Graph) {
        adapterDataTop = object : com.awp.samakaki.utils.AbstractGraphAdapter<NodeViewHolderDataTop>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolderDataTop {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.node, parent, false)
                return NodeViewHolderDataTop(view)
            }

            override fun onBindViewHolder(holder: NodeViewHolderDataTop, position: Int) {
                Glide.with(holder.itemView.context)
                    .load(R.drawable.dummy_avatar)
                    .centerInside()
                    .into(holder.imgProfile)
                holder.textView.text = Objects.requireNonNull(getNodeData(position)).toString()
            }
        }.apply {
            this.submitGraph(graph)
            recyclerViewDataTop.adapter = this
        }
    }


    protected inner class NodeViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.name)
        var imgProfile: ImageView = itemView.findViewById(R.id.img_profile)

        init {
            itemView.setOnClickListener {
                currentNode = adapter.getNode(bindingAdapterPosition)
                Snackbar.make(itemView, "Clicked on " + adapter.getNodeData(bindingAdapterPosition)?.toString(),
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    protected inner class NodeViewHolderDataTop internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.name)
        var imgProfile: ImageView = itemView.findViewById(R.id.img_profile)

        init {
            itemView.setOnClickListener {
                currentNode = adapter.getNode(bindingAdapterPosition)
                Snackbar.make(itemView, "Clicked on " + adapterDataTop.getNodeData(bindingAdapterPosition)?.toString(),
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    protected val nodeText: String
        get() = "Node " + nodeCount++

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun stopLoading() {
        binding.prgbar.visibility = View.GONE
    }

    fun showLoading() {
        binding.prgbar.visibility = View.VISIBLE
    }

    private fun textMessage(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }
}