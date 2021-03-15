package com.rakaneth.extensions

import com.rakaneth.engine.GameState
import com.rakaneth.engine.effect.StatChanger
import com.rakaneth.entity.Entity
import com.rakaneth.entity.component.*
import com.rakaneth.map.GameMap
import squidpony.squidmath.Coord

fun Entity.canSee(mx: Int, my: Int): Boolean {
    return getComponent(VisionComponent::class).fold(
        whenEmpty = { false },
        whenPresent = { visionComponent ->
            visionComponent.canSee(mx, my)
        })
}

fun Entity.canSee(other: Entity): Boolean {
    return canSee(other.x, other.y) && other.mapID == this.mapID
}

fun Entity.resetVision(map: GameMap) {
    whenHas(VisionComponent::class) {
        it.visible = map.visionMap
        it.updateFOV(map, this)
    }
}

fun Entity.whenInPlayerSight(fn: (Entity) -> Unit) {
    if (GameState.playerCanSee(this)) {
        fn(this)
    }
}

fun Entity.whenIsPlayer(fn: (Entity) -> Unit) {
    whenHas(PlayerComponent::class) {
        fn(this)
    }
}

fun Entity.resetSpell() {
    this.whenHas(CasterComponent::class) {
        it.resetSpell(this)
    }
}

fun Entity.getStat(
    combatSelector: (CombatantComponent) -> Int,
    statChangeSelector: (StatChanger) -> Int
): Int {
    val rawTotal = this.getComponent(CombatantComponent::class).fold(
        whenEmpty = { 0 },
        whenPresent = combatSelector
    )
    val effTotal = this.getComponent(EffectComponent::class).fold(
        whenEmpty = { 0 },
        whenPresent = { it.sumOfEffects(statChangeSelector) })

    return rawTotal + effTotal
}

val Entity.obscuredName: String
    get() = if (GameState.playerCanSee(this)) this.name else "Something"

val Entity.isPlayer: Boolean
    get() = getComponent(PlayerComponent::class).isPresent

val Entity.spd
    get() = this.getStat(
        combatSelector = { it.spd },
        statChangeSelector = { it.spd })

val Entity.will
    get() = this.getStat(
        combatSelector = { it.will },
        statChangeSelector = { it.will })

val Entity.atk
    get() = this.getStat(
        combatSelector = { it.atk },
        statChangeSelector = { it.atk })

val Entity.dfp
    get() = this.getStat(
        combatSelector = { it.dfp },
        statChangeSelector = { it.dfp }
    )

val Entity.tou
    get() = this.getStat(
        combatSelector = { it.tou },
        statChangeSelector = { it.tou })

val Entity.dmg
    get() = this.getStat(
        combatSelector = { it.dmg },
        statChangeSelector = { it.dmg })
