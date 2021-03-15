package com.rakaneth.engine.action

import com.rakaneth.engine.Messenger
import com.rakaneth.entity.Entity
import org.hexworks.cobalt.datatypes.Maybe

class WaitAction(override val actor: Entity) : GameAction {
    override val cost: Int = 100

    override fun perform(): Maybe<GameAction> {
        Messenger.addMessage("${actor.name} waits.", actor)
        return Maybe.empty()
    }
}