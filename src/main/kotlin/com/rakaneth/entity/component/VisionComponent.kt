package com.rakaneth.entity.component

import com.rakaneth.entity.Entity
import com.rakaneth.map.GameMap
import squidpony.squidgrid.FOV
import squidpony.squidgrid.Radius

class VisionComponent(var visible: Array<DoubleArray>, var vision: Double) : Component {
    override val name: String = "vision"

    fun updateFOV(map: GameMap, entity: Entity) {
        FOV.reuseFOVSymmetrical(map.resistances, visible, entity.x, entity.y, vision, Radius.DIAMOND)
        entity.whenHas(PlayerComponent::class) { map.updateExplored(visible) }
    }

    fun canSee(x: Int, y: Int): Boolean = visible[x][y] > 0.0
}