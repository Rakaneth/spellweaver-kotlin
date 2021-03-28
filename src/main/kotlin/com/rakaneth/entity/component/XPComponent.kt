package com.rakaneth.entity.component

import com.rakaneth.engine.GameState
import com.rakaneth.engine.Messenger
import com.rakaneth.entity.Entity
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.internal.binding.ComputedBinding

class XPComponent(val entity: Entity): Component {
    override val name: String = "xp"

    private val strProp = createPropertyFrom(0)
    private val stamProp = createPropertyFrom(0)
    private val sklProp = createPropertyFrom(0)
    private val sagProp = createPropertyFrom(0)

    val strLvlProp = ComputedBinding(strProp) { levelOf(it) }.apply {
        onChange { ovc ->
            if (ovc.oldValue < ovc.newValue) {
                val levels = ovc.newValue - ovc.oldValue
                entity.whenHas(CombatantComponent::class) { cc ->
                    cc.atk += levels
                    cc.dmg += levels
                    Messenger.addMessage("${entity.name} grows stronger!", entity)
                }
            }
        }
    }

    val stamLvlProp = ComputedBinding(stamProp) { levelOf(it) }.apply {
        onChange { ovc ->
            if (ovc.oldValue < ovc.newValue) {
                val levels = ovc.newValue - ovc.oldValue
                entity.whenHas(CombatantComponent::class) { cc ->
                    cc.tou += levels
                }
                entity.whenHas(VitalsComponent::class) { vit ->
                    for (i in ovc.oldValue+1..ovc.newValue) {
                        vit.maxHP += (5 * i)
                    }
                }
                Messenger.addMessage("${entity.name} grows tougher!", entity)
            }
        }
    }

    val sklLvlProp = ComputedBinding(sklProp) { levelOf(it) }.apply {
        onChange {ovc ->
            if (ovc.oldValue < ovc.newValue) {
                val levels = ovc.newValue - ovc.oldValue
                entity.whenHas(CombatantComponent::class) { cc ->
                    cc.dfp += levels
                }
                Messenger.addMessage("${entity.name} grows more skillful!", entity)
            }
        }
    }

    val sagLevelProp = ComputedBinding(sagProp) { levelOf(it) }.apply {
        onChange { ovc ->
            if (ovc.oldValue < ovc.newValue) {
                val levels = ovc.newValue - ovc.oldValue
                entity.whenHas(CombatantComponent::class) { cc ->
                    cc.will += levels
                    entity.whenHas(CasterComponent::class) { cast ->
                        for (i in ovc.oldValue+1..ovc.newValue) {
                            cast.maxPwr += (5 * i)
                            if (i and 1 == 0) {
                                cast.pwrPerTurn += 1
                            }
                        }
                    }
                }
                Messenger.addMessage("${entity.name} expands their power!", entity)
            }
        }
    }

    var strXP: Int by strProp.asDelegate() //MECHANICS: gain dmg XP when bumping
    var stamXP: Int by stamProp.asDelegate() //MECHANICS: gain dmg / 5 XP when taking damage, 10 on phys effect
    var sklXP: Int by sklProp.asDelegate() //MECHANICS: gain 1 XP when bumped
    var sagXP: Int by sagProp.asDelegate() //MECHANICS: gain 1 XP per Pwr spent

    companion object {
        private val FIB: List<Int> = listOf(
            0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765)
        fun levelOf(score: Int) = FIB.indexOfLast { score >= it }
    }

}