package com.awp.samakaki.utils


internal class BuchheimWalkerNodeData {
    lateinit var ancestor: com.awp.samakaki.utils.Node
    var thread: com.awp.samakaki.utils.Node? = null
    var number: Int = 0
    var depth: Int = 0
    var prelim: Double = 0.toDouble()
    var modifier: Double = 0.toDouble()
    var shift: Double = 0.toDouble()
    var change: Double = 0.toDouble()
}