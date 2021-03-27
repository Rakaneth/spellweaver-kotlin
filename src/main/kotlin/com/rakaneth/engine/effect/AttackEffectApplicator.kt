package com.rakaneth.engine.effect

import com.rakaneth.entity.Entity

fun interface AttackEffectApplicator<T: Effect> {
    fun apply(attacker: Entity, defender: Entity): T
}