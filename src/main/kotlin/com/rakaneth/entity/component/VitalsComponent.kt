package com.rakaneth.entity.component

import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom

class VitalsComponent(startHP: Int): Component {
    override val name: String = "vitals"
    val hpProp = createPropertyFrom(startHP)
    val maxHPProp = createPropertyFrom(startHP)

    var hp: Int by hpProp.asDelegate()
    var maxHP: Int by maxHPProp.asDelegate()

    val isAlive: Boolean
        get() = hp > 0
}