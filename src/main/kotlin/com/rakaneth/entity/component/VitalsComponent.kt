package com.rakaneth.entity.component

import com.rakaneth.engine.GameElement
import com.rakaneth.engine.Messenger
import com.rakaneth.entity.Entity
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.internal.binding.ComputedBinding
import org.hexworks.cobalt.databinding.internal.binding.ComputedDualBinding

class VitalsComponent(startHP: Int,
                      var weakness: GameElement = GameElement.None,
                      var resistance: GameElement = GameElement.None) : Component {
    override val name: String = "vitals"

    private val hpProp = createPropertyFrom(startHP)
    private val maxHPProp = createPropertyFrom(startHP)
    val hpStringProp = ComputedDualBinding(hpProp, maxHPProp) { hp, maxHP -> "HP: $hp/$maxHP"}

    var hp: Int by hpProp.asDelegate()
    var maxHP: Int by maxHPProp.asDelegate()

    val isAlive: Boolean
        get() = hp > 0

    private fun isWeak(element: GameElement): Boolean = weakness == element
    private fun isResistant(element: GameElement): Boolean = resistance == element
    private fun applyWeakness(amt: Int): Int = (amt * 1.5).toInt()
    private fun applyResistance(amt: Int): Int = amt / 2

    fun takeDamage(bearer: Entity, dmg: Int, element: GameElement) {
        val amt: Int = when {
            //MECHANICS: weakness 150%, resistance 50%
            isWeak(element) -> {
                Messenger.addMessage("${bearer.name} is WEAK against ${element.eleName} damage!", bearer)
                applyWeakness(dmg)
            }
            isResistant(element) -> {
                Messenger.addMessage("${bearer.name} is RESISTANT to ${element.eleName} damage!", bearer)
                applyResistance(dmg)
            }
            else -> dmg
        }
        hp -= amt
        Messenger.addMessage("${bearer.name} takes $amt ${element.name} damage", bearer)
    }

    fun heal() { hp = maxHP }
}