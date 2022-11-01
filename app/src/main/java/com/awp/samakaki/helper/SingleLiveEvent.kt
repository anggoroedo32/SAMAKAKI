package com.awp.samakaki.helper

open class SingleLiveEvent<out T>(private val context: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled() : T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            context
        }
    }

    fun peekContent() : T  = context

}