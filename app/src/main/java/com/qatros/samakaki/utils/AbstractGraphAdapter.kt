package com.qatros.samakaki.utils

import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractGraphAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    var graph: com.qatros.samakaki.utils.Graph? = null
    override fun getItemCount(): Int = graph?.nodeCount ?: 0

    open fun getNode(position: Int): com.qatros.samakaki.utils.Node? = graph?.getNodeAtPosition(position)
    open fun getNodeData(position: Int): Any? = graph?.getNodeAtPosition(position)?.data

    /**
     * Submits a new graph to be displayed.
     *
     *
     * If a graph is already being displayed, you need to dispatch Adapter.notifyItem.
     *
     * @param graph The new graph to be displayed.
     */
    open fun submitGraph(@Nullable graph: com.qatros.samakaki.utils.Graph?) {
        this.graph = graph
    }
}