package com.rakaneth.engine

import com.rakaneth.engine.effect.Effect
import com.rakaneth.engine.effect.InstantDamageEffect
import com.rakaneth.engine.effect.SpellEffectApplicator
import com.rakaneth.extensions.ZERO_COORD
import squidpony.squidai.PointAOE
import squidpony.squidai.Technique
import squidpony.squidmath.Coord


class Spell(
    var origin: Coord = ZERO_COORD,
    var tiles: Array<CharArray>
) : Technique("spell", PointAOE(origin)) {
    init {
        setMap(tiles)
    }

    var center: Coord = ZERO_COORD
    val elements: MutableSet<GameElement> = mutableSetOf()
    var range: Int = 1
    var potency: Int = 1
    var radius: Int = 1
    var cost: Int = 0
    val goodEffects: MutableList<SpellEffectApplicator<*>> = mutableListOf()
    val badEffects: MutableList<SpellEffectApplicator<*>> = mutableListOf()
    private var baseElement: GameElement = GameElement.None

    fun charge(element: GameElement) {
        if (elements.contains(element)) return //MECHANICS: can't use same element in a combo
        when (elements.size) {
            0 -> firstCast(element)
            1 -> secondCast(element)
            2 -> thirdCast(element)
            else -> return
        }
        elements.add(element)
    }

    private fun firstCast(element: GameElement) {
        cost = element.baseCost
        baseElement = element
        when (element) {
            GameElement.Fire -> {
                val fireDmg = SpellEffectApplicator { spell, caster, target ->
                    InstantDamageEffect(spell.potency * 2, GameElement.Fire)
                }
                badEffects.add(fireDmg)
            }
        }
    }

    private fun secondCast(element: GameElement) {
        cost += element.secondCost
    }

    private fun thirdCast(element: GameElement) {
        cost += element.thirdCost
    }


}