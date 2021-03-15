package com.rakaneth.engine

import com.rakaneth.entity.Entity
import com.rakaneth.extensions.isPlayer
import com.rakaneth.extensions.spd

class ActorQueue(actors: List<Entity>) {
    init {
        actors.forEach { actor ->
            queue.add(ActorTurn(actor, gameTurn, gameTick))
        }
    }

    private val queue: MutableList<ActorTurn> = mutableListOf()
    private var gameTurn: Int = 1
    private var gameTick: Int = 0

    data class ActorTurn(val actor: Entity, var round: Int, var tick: Int) : Comparable<ActorTurn> {
        operator fun plusAssign(ticks: Int) {
            val newTurn = (tick + ticks) / 100
            val newTick = (tick + ticks) % 100
            round = newTurn
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

    fun update() {
        while (true) {
            val lastTurn = gameTurn
            val curActorTurn = next()
            val curActor = curActorTurn.actor
            if (curActor.isPlayer) break
            val cost = act(curActor)
            curActorTurn += cost
            if (gameTurn > lastTurn) {
                //GameState.tick(gameTurn - lastTurn)
            }
        }
    }


}