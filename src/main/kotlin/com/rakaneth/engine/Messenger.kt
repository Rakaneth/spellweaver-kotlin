package com.rakaneth.engine

object Messenger {
    val messages: MutableList<String> = mutableListOf()
    fun addMessage(msg: String) {
        messages.add(msg)
    }
}