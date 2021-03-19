package com.rakaneth.entity.component

import com.rakaneth.engine.GameElement
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

    fun isWeak(element: GameElement): Boolean = weakness == element
    fun isResistant(element: GameElement): Boolean = resistance == element
    fun applyWeakness(amt: Int): Int = (amt * 1.5).toInt()
    fun applyResistance(amt: Int): Int = amt / 2

    fun takeDamage(dmg: Int, element: GameElement) {
        val amt: Int = when {
            //MECHANICS: weakness 150%, resistance 50%
            isWeak(element) -> applyWeakness(dmg)
            isResistant((element)) -> applyResistance(dmg)
            else -> dmg
        }
        hp -= amt
    }
}