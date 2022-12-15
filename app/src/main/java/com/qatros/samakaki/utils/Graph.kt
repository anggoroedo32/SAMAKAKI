package com.qatros.samakaki.utils

class Graph {
    private val _nodes = arrayListOf<Node>()
    private val _edges = arrayListOf<Edge>()
    val nodes: List<Node> = _nodes
    val edges: List<Edge> = _edges

    private var isTree = false

    val nodeCount: Int
        get() = _nodes.size

    fun addNode(node: Node) {
        if (node !in _nodes) {
            _nodes.add(node)
        }
    }

    fun addNodes(vararg nodes: Node) = nodes.forEach { addNode(it) }

    fun removeNode(node: Node) {
        if (!_nodes.contains(node)) {
            throw IllegalArgumentException("Unable to find node in graph.")
        }

        if (isTree) {
            for (n in successorsOf(node)) {
                removeNode(n)
            }
        }

        _nodes.remove(node)

        val iterator = _edges.iterator()
        while (iterator.hasNext()) {
            val (source, destination) = iterator.next()
            if (source == node || destination == node) {
                iterator.remove()
            }
        }
    }

    fun removeNodes(vararg nodes: com.qatros.samakaki.utils.Node) = nodes.forEach { removeNode(it) }

    fun addEdge(source: com.qatros.samakaki.utils.Node, destination: com.qatros.samakaki.utils.Node): com.qatros.samakaki.utils.Edge {
        val edge = com.qatros.samakaki.utils.Edge(source, destination)
        addEdge(edge)

        return edge
    }

    fun addEdge(edge: com.qatros.samakaki.utils.Edge) {
        addNode(edge.source)
        addNode(edge.destination)

        if (edge !in _edges) {
            _edges.add(edge)
        }
    }

    fun addEdges(vararg edges: com.qatros.samakaki.utils.Edge) = edges.forEach { addEdge(it) }

    fun removeEdge(edge: com.qatros.samakaki.utils.Edge) = _edges.remove(edge)

    fun removeEdges(vararg edges: com.qatros.samakaki.utils.Edge) = edges.forEach { removeEdge(it) }

    fun removeEdge(predecessor: Node, current: Node) {
        val iterator = _edges.iterator()
        while (iterator.hasNext()) {
            val (source, destination) = iterator.next()
            if (source == predecessor && destination == current) {
                iterator.remove()
            }
        }
    }

    fun hasNodes(): Boolean = _nodes.isNotEmpty()

    fun getNodeAtPosition(position: Int): com.qatros.samakaki.utils.Node {
        if (position < 0) {
            throw IllegalArgumentException("position can't be negative")
        }

        val size = _nodes.size
        if (position >= size) {
            throw IndexOutOfBoundsException("Position: $position, Size: $size")
        }

        return _nodes[position]
    }

    fun getEdgeBetween(source: com.qatros.samakaki.utils.Node, destination: com.qatros.samakaki.utils.Node): com.qatros.samakaki.utils.Edge? =
        _edges.find { it.source == source && it.destination == destination }

    fun hasSuccessor(node: com.qatros.samakaki.utils.Node): Boolean = _edges.any { it.source == node }

    fun successorsOf(node: Node) =
        _edges.filter { it.source == node }.map { edge -> edge.destination }

    fun hasPredecessor(node: Node): Boolean = _edges.any { it.destination == node }

    fun predecessorsOf(node: Node) =
        _edges.filter { it.destination == node }.map { edge -> edge.source }

    operator fun contains(node: com.qatros.samakaki.utils.Node): Boolean = _nodes.contains(node)
    operator fun contains(edge: com.qatros.samakaki.utils.Edge): Boolean = _edges.contains(edge)

    fun containsData(data: Any) = _nodes.any { it.data == data }

    fun getNodeAtPosition(data: Any) = _nodes.find { node -> node.data == data }

    fun getOutEdges(node: com.qatros.samakaki.utils.Node): List<com.qatros.samakaki.utils.Edge> = _edges.filter { it.source == node }

    fun getInEdges(node: com.qatros.samakaki.utils.Node): List<com.qatros.samakaki.utils.Edge> = _edges.filter { it.destination == node }

    // Todo this is a quick fix and should be removed later
    internal fun setAsTree(isTree: Boolean) {
        this.isTree = isTree
    }
}
