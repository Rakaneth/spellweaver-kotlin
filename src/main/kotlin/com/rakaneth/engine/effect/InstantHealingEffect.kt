package com.rakaneth.engine.effect

import com.rakaneth.entity.Entity
import com.rakaneth.entity.component.VitalsComponent

class InstantHealingEffect(private val amt: Int)
    : Effect("Healing", INSTANT, ResistType.NONE) {
    override fun onApply(bearer: Entity) {
        bearer.whenHas(VitalsComponent::class) {
            it.hp = (it.hp + amt).coerceAtMost(it.maxHP)
        }
    }

    override fun reportApply(bearer: Entity): String {
        return "${bearer.name} heals for $amt."
    }
}