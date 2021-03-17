package com.rakaneth.entity.component

import org.hexworks.cobalt.databinding.api.binding.bindPlusWith
import org.hexworks.cobalt.databinding.api.binding.bindTransform
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.internal.binding.ComputedDualBinding

class CombatantComponent(
    atk: Int,
    dmg: Int,
    dfp: Int,
    tou: Int,
    will: Int,
    spd: Int
) : Component {
    override val name: String = "combatant"
    var atk: Int = 0
    var dmg: Int = 0
    var dfp: Int = 0
    var tou: Int = 0
    var will: Int = 0
    var spd: Int = 0
}