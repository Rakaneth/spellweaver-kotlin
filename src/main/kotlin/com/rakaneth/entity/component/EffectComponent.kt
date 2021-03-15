package com.rakaneth.entity.component

import com.rakaneth.engine.effect.Effect
import com.rakaneth.engine.effect.StatChanger
import org.hexworks.cobalt.datatypes.Maybe

class EffectComponent : Component {
    override val name: String = "effects"
    private val effects: MutableMap<String, Effect> = mutableMapOf()

    fun getEffect(name: String): Maybe<Effect> = Maybe.ofNullable(effects[name])
    fun addEffect(eff: Effect) {
        effects.putIfAbsent(eff.name, eff)
    }

    fun removeEffect(effName: String) {
        effects.remove(effName)
    }

    fun removeEffect(eff: Effect) {
        effects.remove(eff.name, eff)
    }

    fun sumOfEffects(fn: (StatChanger) -> Int): Int {
        return effects.values
            .filterIsInstance(StatChanger::class.java)
            .sumOf(fn)
    }

    //TODO: tryApplyEffect(eff, entity)
}