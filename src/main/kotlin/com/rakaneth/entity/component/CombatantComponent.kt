package com.rakaneth.entity.component

import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom

class CombatantComponent(
    atk: Int,
    dmg: Int,
    dfp: Int,
    tou: Int,
    will: Int,
    spd: Int
) : Component {
    override val name: String = "combatant"

    val atkProp = createPropertyFrom(atk)
    val dmgProp = createPropertyFrom(dmg)
    val dfpProp = createPropertyFrom(dfp)
    val touProp = createPropertyFrom(tou)
    val willProp = createPropertyFrom(will)
    val spdProp = createPropertyFrom(spd)

    var atk: Int by atkProp.asDelegate()
    var dmg: Int by dmgProp.asDelegate()
    var dfp: Int by dfpProp.asDelegate()
    var tou: Int by touProp.asDelegate()
    var will: Int by willProp.asDelegate()
    var spd: Int by spdProp.asDelegate()
}