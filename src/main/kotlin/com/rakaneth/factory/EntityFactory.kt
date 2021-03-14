package com.rakaneth.factory

import com.rakaneth.Swatch.PLAYER_BG
import com.rakaneth.Swatch.PLAYER_FG
import com.rakaneth.engine.GameState
import com.rakaneth.entity.Entity
import com.rakaneth.entity.VisionComponent
import com.rakaneth.extensions.resetVision
import com.rakaneth.map.GameMap
import squidpony.squidmath.Coord

object EntityFactory {
    fun newPlayer(name: String): Entity {
        val newPlayer = Entity.newCreatureBuilder()
            .withName(name)
            .withDesc("Apprentice Spellweaver")
            .withVision(6.0)
            .withBG(PLAYER_BG)
            .withFG(PLAYER_FG)
            .buildPlayer()

        newPlayer.addComponent(VisionComponent(GameState.curMap.visionMap, 6.0))

        return newPlayer
    }

    fun seed(entity: Entity, map: GameMap, pos: Coord) {
        entity.pos = pos
        entity.mapID = map.id
        entity.resetVision(map)
    }

    fun randomSeed(entity: Entity, map: GameMap) {
        seed(entity, map, map.randomFloor())
    }
}