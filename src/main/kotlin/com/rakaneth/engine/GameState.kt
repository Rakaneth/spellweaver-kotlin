package com.rakaneth.engine

import com.rakaneth.entity.Entity
import com.rakaneth.entity.PlayerComponent
import com.rakaneth.entity.VisionComponent
import com.rakaneth.extensions.canSee
import com.rakaneth.extensions.resetVision
import com.rakaneth.factory.EntityFactory
import com.rakaneth.map.GameMap

object GameState {
    private val maps: MutableMap<String, GameMap> = mutableMapOf()
    private var curMapID: String = "floor1"
    private val entities: MutableList<Entity> = mutableListOf()

    val curMap: GameMap
        get() = maps[curMapID]!!
    val player: Entity
        get() = entities.first { it.getComponent(PlayerComponent::class).isPresent }
    val currentEntities: List<Entity>
        get() = entities.filter { it.mapID == curMapID }

    fun addEntity(e: Entity) { entities.add(e) }
    fun removeEntity(e: Entity) { entities.remove(e)}
    fun addMap(m: GameMap) { maps.putIfAbsent(m.id, m)}
    fun changeLevel(mapID: String) {
        curMapID = mapID
        player.resetVision(curMap)
    }

    fun playerCanSee(e: Entity) = player.canSee(e)
    fun entitiesInView(e: Entity): List<Entity> {
        return entities.filter { other ->
            other != e && e.canSee(other)
        }
    }

    //TODO: CurrentActors
}