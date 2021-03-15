package com.rakaneth.engine.action

import com.rakaneth.engine.BlockResult
import com.rakaneth.engine.GameState
import com.rakaneth.engine.Messenger
import com.rakaneth.entity.Entity
import org.hexworks.cobalt.datatypes.Maybe
import squidpony.squidmath.Coord

class MoveAction(override val actor: Entity, val to: Coord) : GameAction {
    override val cost: Int = 100

    override fun perform(): Maybe<GameAction> {
        return when (val maybeBlocker = GameState.getBlocker(to.x, to.y)) {
            is BlockResult.EntityBlocker -> Maybe.of(BumpAction(actor, maybeBlocker.entity))
            is BlockResult.MapBlocker -> {
                Messenger.addMessage("${actor.name} runs into something.", actor)
                Maybe.empty()
            }
            is BlockResult.None -> {
                //TODO: pickup items
                actor.pos = to
                Maybe.empty()
            }
        }
    }
}