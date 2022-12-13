package com.qatros.samakaki.utils


data class Node(var data: Any) {
    // TODO make private
    val position: VectorF = com.qatros.samakaki.utils.VectorF()
    val size: com.qatros.samakaki.utils.Size = com.qatros.samakaki.utils.Size()

    var height: Int
        get() = size.height
        set(value) {
            size.height = value
        }

    var width: Int
        get() = size.width
        set(value) {
            size.width = value
        }

    var x: Float
        get() = position.x
        set(value) {
            position.x = value
        }

    var y: Float
        get() = position.y
        set(value) {
            position.y = value
        }

    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun setPosition(position: com.qatros.samakaki.utils.VectorF) {
        this.x = position.x
        this.y = position.y
    }
}
