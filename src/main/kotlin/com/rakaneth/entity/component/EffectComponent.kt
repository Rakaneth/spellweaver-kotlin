package com.rakaneth.entity.component

import com.rakaneth.engine.effect.Effect
import com.rakaneth.engine.effect.StatChangerEffect
import com.rakaneth.entity.Entity
import org.hexworks.cobalt.datatypes.Maybe

class EffectComponent : Component {
    override val name: String = "effects"
    private val effects: MutableMap<String, Effect> = mutableMapOf()

    fun getEffect(name: String): Maybe<Effect> = Maybe.ofNullable(effects[name])
    fun addEffect(eff: Effect) {
        effects.putIfAbsent(eff.name, eff)
    }

    fun stripEffect(bearer: Entity, effName: String) {
        val maybeEffect = effects[effName]
        maybeEffect?.remove(bearer)
        effects.remove(effName)
    }

    fun sumOfEffects(fn: (StatChangerEffect) -> Int): Int {
        return effects.values
            .filterIsInstance(StatChangerEffect::class.java)
            .sumOf(fn)
    }

    fun tick(entity: Entity, ticks: Int) {
        effects.values.forEach { eff ->
            eff.tick(entity, ticks)
            if (eff.isExpired) {
                eff.remove(entity)
            }
        }

        effects.entries.removeIf { entry ->
            entry.value.isExpired
        }
    }

    //TODO: tryApplyEffect(eff, entity)
}