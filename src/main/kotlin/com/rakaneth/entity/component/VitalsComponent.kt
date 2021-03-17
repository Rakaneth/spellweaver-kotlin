package com.rakaneth.entity.component

import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.internal.binding.ComputedBinding
import org.hexworks.cobalt.databinding.internal.binding.ComputedDualBinding

class VitalsComponent(startHP: Int) : Component {
    override val name: String = "vitals"

    private val hpProp = createPropertyFrom(startHP)
    private val maxHPProp = createPropertyFrom(startHP)
    val hpStringProp = ComputedDualBinding(hpProp, maxHPProp) { hp, maxHP -> "HP: $hp/$maxHP"}

    var hp: Int by hpProp.asDelegate()
    var maxHP: Int by maxHPProp.asDelegate()

    val isAlive: Boolean
        get() = hp > 0
}