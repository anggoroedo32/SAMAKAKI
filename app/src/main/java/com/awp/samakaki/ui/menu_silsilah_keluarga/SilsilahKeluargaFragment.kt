package com.awp.samakaki.ui.menu_silsilah_keluarga

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.R
import com.awp.samakaki.databinding.FragmentFamilyBinding
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dev.bandb.graphview.AbstractGraphAdapter
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node
import java.util.*

abstract class SilsilahKeluargaFragment : Fragment() {

    private var _binding: FragmentFamilyBinding? = null
    private val binding get() = _binding!!
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var adapter: com.awp.samakaki.utils.AbstractGraphAdapter<NodeViewHolder>
    private lateinit var fab: FloatingActionButton
    private var currentNode: com.awp.samakaki.utils.Node? = null
    private var nodeCount = 1

    abstract fun createGraph(): com.awp.samakaki.utils.Graph
    abstract fun setLayoutManager()
    abstract fun setEdgeDecoration()

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
        recyclerView = binding.rvFamilyTree
        setLayoutManager()
        setEdgeDecoration()
        setupGraphView(graph)

        setupFab(graph)

        val isHavingData = true

        val familyTree = binding.wrapFamilyTree
        val isiProfil = binding.wrapIsiProfil

        if (isHavingData == true) {
            familyTree.visibility = View.VISIBLE
            isiProfil.visibility = View.GONE
        } else {
            familyTree.visibility = View.GONE
            isiProfil.visibility = View.VISIBLE
        }

    }

    private fun setupGraphView(graph: com.awp.samakaki.utils.Graph) {
        adapter = object : com.awp.samakaki.utils.AbstractGraphAdapter<NodeViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.node, parent, false)
                return NodeViewHolder(view)
            }

            override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
                Glide.with(holder.itemView.context)
                    .load("https://www.themealdb.com/images/ingredients/Lime.png")
                    .centerInside()
                    .into(holder.imgProfile)
                holder.textView.text = Objects.requireNonNull(getNodeData(position)).toString()
            }
        }.apply {
            this.submitGraph(graph)
            recyclerView.adapter = this
        }
    }

    private fun setupFab(graph: com.awp.samakaki.utils.Graph) {
        fab = binding.addNode
        fab.setOnClickListener {
            val newNode = com.awp.samakaki.utils.Node(nodeText)
            if (currentNode != null) {
                graph.addEdge(currentNode!!, newNode)
            } else {
                graph.addNode(newNode)
            }
            adapter.notifyDataSetChanged()
        }
        fab.setOnLongClickListener {
            if (currentNode != null) {
                graph.removeNode(currentNode!!)
                currentNode = null
                adapter.notifyDataSetChanged()
                fab.hide()
            }
            true
        }
    }

    protected inner class NodeViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.name)
        var imgProfile: ImageView = itemView.findViewById(R.id.img_profile)

        init {
            itemView.setOnClickListener {
                if (!fab.isShown) {
                    fab.show()
                }
                currentNode = adapter.getNode(bindingAdapterPosition)
                Snackbar.make(itemView, "Clicked on " + adapter.getNodeData(bindingAdapterPosition)?.toString(),
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
}