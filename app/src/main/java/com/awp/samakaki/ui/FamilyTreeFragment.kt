package com.awp.samakaki.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.awp.samakaki.R
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node
import dev.bandb.graphview.layouts.tree.BuchheimWalkerConfiguration
import dev.bandb.graphview.layouts.tree.BuchheimWalkerLayoutManager
import dev.bandb.graphview.layouts.tree.TreeEdgeDecoration

class FamilyTreeFragment : FamilyFragment() {

    override fun setLayoutManager() {
        val configuration = BuchheimWalkerConfiguration.Builder()
            .setSiblingSeparation(100)
            .setLevelSeparation(100)
            .setSubtreeSeparation(100)
            .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
            .build()
        recyclerView.layoutManager = context?.let { BuchheimWalkerLayoutManager(it, configuration) }
    }

    override fun setEdgeDecoration() {
        recyclerView.addItemDecoration(TreeEdgeDecoration())
    }

    override fun createGraph(): Graph {
        val graph = Graph()
        val node1 = Node(nodeText)
        val node2 = Node(nodeText)
        val node3 = Node(nodeText)
        val node4 = Node(nodeText)
        val node5 = Node(nodeText)
        val node6 = Node(nodeText)
        val node8 = Node(nodeText)
        val node7 = Node(nodeText)
        val node9 = Node(nodeText)
        val node10 = Node(nodeText)
        val node11 = Node(nodeText)
        val node12 = Node(nodeText)
        graph.addEdge(node1, node2)
        graph.addEdge(node1, node3)
        graph.addEdge(node1, node4)
        graph.addEdge(node2, node5)
        graph.addEdge(node2, node6)
        graph.addEdge(node6, node7)
        graph.addEdge(node6, node8)
        graph.addEdge(node4, node9)
        graph.addEdge(node4, node10)
        graph.addEdge(node4, node11)
        graph.addEdge(node11, node12)
        return graph
    }

}