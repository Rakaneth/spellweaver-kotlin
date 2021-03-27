package com.rakaneth.engine.effect

import com.rakaneth.engine.Spell
import com.rakaneth.entity.Entity

fun interface SpellEffectApplicator<T: Effect> {
    fun apply(spell: Spell, caster: Entity, target: Entity): T
}