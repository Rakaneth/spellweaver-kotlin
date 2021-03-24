package com.rakaneth.engine

import com.rakaneth.entity.Entity
import com.rakaneth.entity.component.ActorComponent
import com.rakaneth.entity.component.BlockerComponent
import com.rakaneth.entity.component.EffectComponent
import com.rakaneth.entity.component.PlayerComponent
import com.rakaneth.extensions.canSee
import com.rakaneth.extensions.resetSpell
import com.rakaneth.extensions.resetVision
import com.rakaneth.map.GameMap
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom

sealed class BlockResult {
    data class EntityBlocker(val entity: Entity) : BlockResult()
    data class MapBlocker(val tileChar: Char) : BlockResult()
    object None : BlockResult()
}

object GameState {
    private val maps: MutableMap<String, GameMap> = mutableMapOf()
    private var curMapID: String = "floor1"
    private val entities: MutableList<Entity> = mutableListOf()
    val redrawProp = createPropertyFrom(false)
    val updateProp = createPropertyFrom(false)
    var redraw: Boolean by redrawProp.asDelegate()
    var update: Boolean by updateProp.asDelegate()

    val curMap: GameMap
        get() = maps[curMapID]!!
    val player: Entity
        get() = entities.first { it.getComponent(PlayerComponent::class).isPresent }
    val currentEntities: List<Entity>
        get() = entities.filter { it.mapID == curMapID }

    init {
        updateProp.onChange {
            if (it.newValue) {
                queue.update()
                updateProp.updateValue(false)
            }
        }
    }

    lateinit var queue: ActorQueue

    fun tick(ticks: Int) {
        currentEntities.forEach { entity ->
            entity.whenHas(EffectComponent::class) { eff ->
                eff.tick(entity, ticks)
            }
        }
    }

    fun addEntity(e: Entity) {
        entities.add(e)
    }

    fun removeEntity(e: Entity) {
        queue.remove(e)
        entities.remove(e)
    }

    fun addMap(m: GameMap) {
        maps.putIfAbsent(m.id, m)
    }

    fun changeLevel(mapID: String) {
        curMapID = mapID
        player.mapID = mapID
        player.resetVision(curMap)
        player.resetSpell()
        queue = ActorQueue(currentEntities.filter { it.has(ActorComponent::class)})
    }

    fun playerCanSee(e: Entity) = player.canSee(e)
    fun entitiesInView(e: Entity): List<Entity> {
        return entities.filter { other ->
            other != e && e.canSee(other)
        }
    }

    fun getBlocker(x: Int, y: Int, mapID: String): BlockResult {
        val map = maps[mapID]!!
        val maybeBlocker = entities.firstOrNull {
            it.mapID == mapID
                    && it.x == x
                    && it.y == y
                    && it.has(BlockerComponent::class)
        }

        return if (maybeBlocker == null) {
            if (map.isBlocking(x, y)) {
                BlockResult.MapBlocker(map.rawTile(x, y))
            } else {
                BlockResult.None
            }
        } else {
            BlockResult.EntityBlocker(maybeBlocker)
        }
    }

    fun getBlocker(x: Int, y: Int): BlockResult = getBlocker(x, y, curMapID)
    fun getMap(mapID: String): GameMap = maps[mapID]!!

    //TODO: CurrentActors
}