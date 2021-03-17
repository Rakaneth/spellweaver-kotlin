package com.rakaneth.entity.component

import com.rakaneth.engine.GameState
import com.rakaneth.engine.Spell
import com.rakaneth.entity.Entity
import com.rakaneth.extensions.will
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.internal.binding.ComputedDualBinding

class CasterComponent(pwr: Int): Component {
    override val name: String = "caster"
    lateinit var spell: Spell
    private val pwrProp = createPropertyFrom(pwr)
    private val maxPwrProp = createPropertyFrom(pwr)
    val pwrStringProp = ComputedDualBinding(pwrProp, maxPwrProp) { pwr, maxPwr -> "Power: $pwr/$maxPwr"}

    var pwr: Int by pwrProp.asDelegate()
    var maxPwr: Int by pwrProp.asDelegate()

    fun resetSpell(caster: Entity) {
        spell = Spell(caster.pos, GameState.getMap(caster.mapID).tiles)
        spell.potency = caster.will
        spell.range = (caster.will / 2).coerceAtLeast(1)
        spell.radius = (caster.will / 3).coerceAtLeast(1)
    }
}