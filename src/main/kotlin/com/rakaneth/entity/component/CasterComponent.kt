package com.rakaneth.entity.component

import com.rakaneth.engine.GameState
import com.rakaneth.engine.Spell
import com.rakaneth.entity.Entity
import com.rakaneth.extensions.will

class CasterComponent: Component {
    override val name: String = "caster"
    lateinit var spell: Spell

    fun resetSpell(caster: Entity) {
        spell = Spell(caster.pos, GameState.getMap(caster.mapID).tiles)
        spell.potency = caster.will
        spell.range = (caster.will / 2).coerceAtLeast(1)
        spell.radius = (caster.will / 3).coerceAtLeast(1)
    }
}