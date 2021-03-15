package com.rakaneth.engine.action

import com.rakaneth.entity.Entity
import org.hexworks.cobalt.datatypes.Maybe

interface GameAction {
    val actor: Entity
    val cost: Int

    fun perform(): Maybe<GameAction>

    fun doAction(): Int {
        var maybeAction = perform()
        var lastCost = cost
        while (maybeAction.isPresent) {
            lastCost = maybeAction.get().cost
            maybeAction = maybeAction.get().perform()
        }
        return lastCost
    }
}