package com.rakaneth.entity.component

import com.rakaneth.engine.GameElement
import org.hexworks.cobalt.databinding.api.binding.bindPlusWith
import org.hexworks.cobalt.databinding.api.binding.bindTransform
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.internal.binding.ComputedDualBinding

class CombatantComponent(
    var atk: Int = 0,
    var dmg: Int = 0,
    var dfp: Int = 0,
    var tou: Int = 0,
    var will: Int = 0,
    var spd: Int = 0,
    override val name: String = "combatant"
) : Component