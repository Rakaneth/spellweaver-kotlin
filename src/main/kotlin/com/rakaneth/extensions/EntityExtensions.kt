package com.rakaneth.extensions

import com.rakaneth.entity.Entity
import com.rakaneth.entity.component.VisionComponent
import com.rakaneth.map.GameMap

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