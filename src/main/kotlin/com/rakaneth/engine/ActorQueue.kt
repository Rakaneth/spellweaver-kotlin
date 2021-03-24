package com.rakaneth.engine

import com.rakaneth.engine.action.GameAction
import com.rakaneth.entity.Entity
import com.rakaneth.extensions.isPlayer
import com.rakaneth.extensions.spd

class ActorQueue(actors: List<Entity>) {
    private val queue: MutableList<ActorTurn> = mutableListOf()
    private var gameTurn: Int = 1
    private var gameTick: Int = 0

    init {
        actors.forEach { actor ->
            queue.add(ActorTurn(actor, gameTurn, gameTick))
        }
    }

    data class ActorTurn(val actor: Entity, var round: Int, var tick: Int) : Comparable<ActorTurn> {
        operator fun plusAssign(ticks: Int) {
            val newTurn = (tick + ticks) / 100
            val newTick = (tick + ticks) % 100
            round += newTurn
            tick = newTick
        }

        override fun compareTo(other: ActorTurn): Int {
            return if (this.round == other.round) {
                if (this.tick == other.tick) {
                    this.actor.spd.compareTo(other.actor.spd)
                } else {
                    this.tick.compareTo(other.tick)
                }
            } else {
                this.round.compareTo(other.round)
            }
        }
    }

    private fun next(): ActorTurn {
        val nextActor = queue.minOrNull() ?: throw IllegalStateException("No actors in ActorQueue")
        gameTurn = nextActor.round
        gameTick = nextActor.tick
        return nextActor
    }

    private fun act(e: Entity): Int {
        /*
        e.whenHas(AIComponent::class) {
            val action = it.ai.chooseAction
            return max(action.doAction() - e.spd, 1)
        }
         */
        return 0
    }

    /**
     * Handles the player's actions, which are not processed here.
     * `entity` should be the player most of the time since NPCs
     * will be handled by {@link #act()}.
     */
    fun actWithAction(entity: Entity, action: GameAction) {
        val maybeTurn = queue.find{ t -> t.actor == entity} ?: return
        val cost =  (action.doAction() - entity.spd).coerceAtLeast(1)
        maybeTurn += cost
    }

    fun add(entity: Entity) {
        //MECHANICS: creatures added mid-stream (summoned guys) act on the following turn
        queue.add(ActorTurn(entity, gameTurn + 1, 0))
    }

    fun remove(entity: Entity) {
        queue.removeIf { it.actor == entity }
    }

    fun update() {
        while (true) {
            val lastTurn = gameTurn
            val curActorTurn = next()
            val curActor = curActorTurn.actor
            if (curActor.isPlayer) break
            val cost = act(curActor)
            curActorTurn += cost
            if (gameTurn > lastTurn) {
                GameState.tick(gameTurn - lastTurn)
            }
        }
    }


}