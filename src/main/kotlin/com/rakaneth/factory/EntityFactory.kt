package com.rakaneth.factory

import com.rakaneth.entity.Entity
import com.rakaneth.entity.component.*
import com.rakaneth.extensions.resetVision
import com.rakaneth.extensions.will
import com.rakaneth.map.GameMap
import org.hexworks.cobalt.datatypes.Maybe
import org.slf4j.LoggerFactory
import squidpony.squidmath.Coord
import java.awt.Color

object EntityFactory {
    private val logger = LoggerFactory.getLogger(EntityFactory::class.java)
    val creatureBP = DataReader.loadCreatures()
    val tier1s = creatureBP.createProbabilityTable { it.level == 0 }
    val tier2s = creatureBP.createProbabilityTable { it.level == 1 }
    val tier3s = creatureBP.createProbabilityTable { it.level == 2 }

    private fun colorFromString(rgb: String): Maybe<Color> {
        val rgbSplit = rgb.split(',')
        if (rgbSplit.size != 3) {
            return Maybe.empty()
        }
        val (r, g, b) = rgbSplit
        return Maybe.of(Color(r.toInt(), g.toInt(), b.toInt()))
    }

    private fun baseCreature(bp: CreatureBlueprint, name: String): Entity {
        val fg = colorFromString(bp.color).fold(
            whenEmpty = {
                logger.error("Failed to parse color ${bp.color} for creature ${bp.name}.")
                Color.BLACK
            },
            whenPresent = { it })
        return Entity.newCreatureBuilder()
            .withName(if (name.isNotEmpty()) name else bp.name)
            .withDesc(bp.desc)
            .withFG(fg)
            .withGlyph(bp.glyph)
            .buildCreature()
    }

    /**
     * Creates a creature with build ID `buildID` in `creatures.yml.`
     * make sure to seed creatures with the `seed` or `seedRandom` functions.
     */
    fun creatureFromBP(buildID: String, name: String = ""): Entity {
        val bp = creatureBP.table[buildID]!!
        val foetus = baseCreature(bp, name)
        val vitals = VitalsComponent(bp.hp)
        val stats = CombatantComponent(bp.atk, bp.dmg, bp.dfp, bp.tou, bp.will, bp.spd)
        val vision = VisionComponent(Array(1) { DoubleArray(1) }, bp.vision)
        val actor = ActorComponent()
        val effects = EffectComponent()
        foetus.addMany(vitals, stats, vision, effects, actor)
        return foetus
    }

    fun newPlayer(name: String): Entity {
        val newPlayer = creatureFromBP("player", name)
        newPlayer.addComponent(PlayerComponent())
        newPlayer.addComponent(CasterComponent(newPlayer.will))
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