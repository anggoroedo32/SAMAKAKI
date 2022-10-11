package com.awp.samakaki.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.awp.samakaki.R
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dev.bandb.graphview.AbstractGraphAdapter
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node
import java.util.*

abstract class FamilyTreeActivity : AppCompatActivity() {

    protected lateinit var recyclerView: RecyclerView
    protected lateinit var adapter: AbstractGraphAdapter<NodeViewHolder>
    private lateinit var fab: FloatingActionButton
    private var currentNode: Node? = null
    private var nodeCount = 1

    abstract fun createGraph(): Graph
    abstract fun setLayoutManager()
    abstract fun setEdgeDecoration()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_tree)

        val graph = createGraph()
        recyclerView = findViewById(R.id.rv_family_tree)
        setLayoutManager()
        setEdgeDecoration()
        setupGraphView(graph)

        setupFab(graph)
    }

    private fun setupGraphView(graph: Graph) {
        adapter = object : AbstractGraphAdapter<NodeViewHolder>() {
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

    private fun setupFab(graph: Graph) {
        fab = findViewById(R.id.addNode)
        fab.setOnClickListener {
            val newNode = Node(nodeText)
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

    protected val nodeImg: Int
        get() = R.drawable.dummy_avatar

    protected val nodeText: String
        get() = "Node " + nodeCount++
}