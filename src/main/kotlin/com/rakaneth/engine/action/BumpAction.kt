package com.rakaneth.engine.action

import com.rakaneth.engine.Messenger
import com.rakaneth.entity.Entity
import com.rakaneth.extensions.obscuredName
import org.hexworks.cobalt.datatypes.Maybe

class BumpAction(override val actor: Entity, val victim: Entity) : GameAction {
    override val cost: Int = 100

    override fun perform(): Maybe<GameAction> {
        //TODO: replace with improved bumps
        Messenger.addMessage("${actor.obscuredName} bumps ${victim.obscuredName}", actor, victim)
        return Maybe.empty()
    }
}