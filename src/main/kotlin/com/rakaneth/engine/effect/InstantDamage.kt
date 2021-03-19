package com.rakaneth.engine.effect

import com.rakaneth.engine.GameElement
import com.rakaneth.entity.Entity
import com.rakaneth.entity.component.VitalsComponent

class InstantDamage(private val dmg: Int, private val element: GameElement)
    : Effect("damage", INSTANT, false) {
    override fun onApply(bearer: Entity) {
        bearer.whenHas(VitalsComponent::class) {
            it.takeDamage(dmg, element)
        }
    }

    override fun reportApply(bearer: Entity): String {
        var result = ""
        bearer.whenHas(VitalsComponent::class) {
            val weakAmt = it.applyWeakness(dmg)
            val resistAmt = it.applyResistance(dmg)
            result =  when {
                it.isWeak(element) -> "${bearer.name} (weak against ${element.name}) takes $weakAmt damage!"
                it.isResistant(element) -> "${bearer.name} (resistant to ${element.name}) takes $resistAmt damage!"
                else -> "${bearer.name} takes $dmg ${element.name} damage!"
            }
        }
        return result
    }
}