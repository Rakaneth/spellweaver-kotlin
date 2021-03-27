package com.rakaneth.engine.effect

import com.rakaneth.engine.GameElement
import com.rakaneth.entity.Entity
import com.rakaneth.entity.component.VitalsComponent

class InstantDamageEffect(private val dmg: Int, private val element: GameElement)
    : Effect("damage", INSTANT, ResistType.NONE) {
    override fun onApply(bearer: Entity) {
        bearer.whenHas(VitalsComponent::class) {
            it.takeDamage(bearer, dmg, element)
        }
    }
}