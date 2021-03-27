package com.rakaneth.engine.effect

import com.rakaneth.engine.GameElement
import com.rakaneth.entity.Entity
import com.rakaneth.entity.component.VitalsComponent

class PoisonEffect(duration: Int, private var stacks: Int)
    : Effect("Poison", duration, ResistType.PHYSICAL) {
    override fun onTick(bearer: Entity, ticks: Int) {
        bearer.whenHas(VitalsComponent::class) {
            it.takeDamage(bearer, stacks, GameElement.Earth)
        }
    }

    override fun onMerge(otherEff: Effect, bearer: Entity) {
        //MECHANICS: Poison stacks intensity
        this.stacks += (otherEff as PoisonEffect).stacks
    }

    override fun reportApply(bearer: Entity): String {
        return "${bearer.name} is poisoned!"
    }

    override fun reportExpire(bearer: Entity): String {
        return "${bearer.name} is no longer poisoned!"
    }

    override fun reportMerge(otherEff: Effect, bearer: Entity): String {
        return "${bearer.name}'s poison worsens!"
    }

    override val modifier: String
        get() = stacks.toString()
}