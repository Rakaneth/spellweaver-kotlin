package com.rakaneth.engine

import com.rakaneth.entity.Entity

object Messenger {
    val messages: MutableList<String> = mutableListOf()

    /**
     * `msg` will be added to the game log if it is not empty and
     * if the player can see any of the `entities` or if no `entities` are given.
     */
    fun addMessage(msg: String, vararg entities: Entity) {
        if (msg.isNotEmpty() && entities.any { GameState.playerCanSee(it) } || entities.isEmpty()) {
            messages.add(msg)
        }
    }
}