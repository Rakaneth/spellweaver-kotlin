package com.rakaneth.engine.effect

import com.rakaneth.engine.Messenger
import com.rakaneth.entity.Entity
import com.rakaneth.entity.component.EffectComponent

open class Effect(val name: String, protected var duration: Int, val resistType: ResistType = ResistType.NONE) {

    enum class ResistType {
        PHYSICAL, MENTAL, NONE
    }

    protected open fun onTick(bearer: Entity, ticks: Int) {}
    protected open fun onApply(bearer: Entity) {}
    protected open fun onExpire(bearer: Entity) {}
    protected open fun onMerge(otherEff: Effect, bearer: Entity) {}
    protected open fun reportTick(bearer: Entity, ticks: Int): String = ""
    protected open fun reportApply(bearer: Entity): String = ""
    protected open fun reportExpire(bearer: Entity): String = ""
    protected open fun reportMerge(otherEff: Effect, bearer: Entity): String = ""
    protected open val modifier: String = ""

    val isExpired: Boolean
        get() = duration == 0

    fun tick(bearer: Entity, ticks: Int) {
        if (duration > 0) {
            onTick(bearer, ticks)
            duration = (duration - ticks).coerceAtLeast(0)
            Messenger.addMessage(reportTick(bearer, ticks), bearer)
        }
    }

    fun apply(bearer: Entity) {
        val maybeComponent = bearer.getComponent(EffectComponent::class)
        maybeComponent.ifPresent {
            it.getEffect(this.name).fold(
                whenEmpty = {
                    this.onApply(bearer)
                    Messenger.addMessage(reportApply(bearer), bearer)
                    if (this.duration > 0) it.addEffect(this)
                },
                whenPresent = { eff ->
                    this.onMerge(eff, bearer)
                    Messenger.addMessage(reportMerge(eff, bearer), bearer)
                }
            )
        }
    }

    fun remove(bearer: Entity) {
        val maybeComponent = bearer.getComponent(EffectComponent::class)
        maybeComponent.ifPresent {
            onExpire(bearer)
            Messenger.addMessage(reportExpire(bearer), bearer)
        }
    }

    override fun toString(): String {
        var output = name
        if (modifier.isNotEmpty()) {
            output += ": $modifier"
        }
        if (duration > 0) {
            output += " ($duration)"
        }
        return output
    }


    companion object {
        const val INFINITE: Int = -1
        const val INSTANT: Int = 0
    }
}