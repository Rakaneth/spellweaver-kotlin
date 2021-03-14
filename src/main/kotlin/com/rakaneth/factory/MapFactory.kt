package com.rakaneth.factory

import com.rakaneth.Swatch
import com.rakaneth.engine.GameState
import com.rakaneth.extensions.component1
import com.rakaneth.extensions.component2
import com.rakaneth.map.GameMap
import squidpony.squidmath.Coord

object MapFactory {
    val firstFloor = GameMap.newBuilder(50, 50)
        .withName("Cellar")
        .withID("floor1")
        .withBoxCarvers(2)
        .withRoundCarvers(3)
        .withDoorPct(85)
        .withDoubleDoors(true)
        .withLight(true)
        .withWallColor(Swatch.STONE_WALL_BG)
        .withFloorColor(Swatch.STONE_FLOOR_BG)
        .withMaxCreatures(20)
        .build()

    val secondFloor = GameMap.newBuilder(50, 50)
        .withName("Second Basement")
        .withBoxCarvers(2)
        .withRoundCarvers(2)
        .withCaveCarvers(1)
        .withID("floor2")
        .withDoorPct(30)
        .withLight(true)
        .withWallColor(Swatch.STONE_WALL_BG)
        .withFloorColor(Swatch.STONE_FLOOR_BG)
        .withMaxCreatures(25)
        .withNamedItem("forceBook")
        .build()

    val thirdFloor = GameMap.newBuilder(75, 50)
        .withName("Caverns")
        .withID("floor3")
        .withWaterPct(15)
        .withCaveCarvers(2)
        .withBoxCarvers(3)
        .withWallColor(Swatch.CAVE_WALL_BG)
        .withFloorColor(Swatch.CAVE_FLOOR_BG)
        .withMaxCreatures(30)
        .withNamedItem("lightBook")
        .build()

    val fourthFloor = GameMap.newBuilder(75, 75)
        .withName("Deep Caverns")
        .withID("floor4")
        .withWaterPct(30)
        .withWallColor(Swatch.CAVE_WALL_BG)
        .withFloorColor(Swatch.CAVE_FLOOR_BG)
        .withCaveCarvers(4)
        .withBoxCarvers(1)
        .withMaxCreatures(35)
        .withNamedItem("darkBook")
        .build()

    val fifthFloor = GameMap.newBuilder(100, 100)
        .withName("Catacombs")
        .withID("floor5")
        .withWaterPct(10)
        .withDoorPct(50)
        .withBoxCarvers(2)
        .withRoundCarvers(2)
        .withCaveCarvers(1)
        .withLight(true)
        .withWallColor(Swatch.CAVE_WALL_BG)
        .withFloorColor(Swatch.STONE_FLOOR_BG)
        .withMaxCreatures(40)
        .withNamedItem("masterBook")
        .withNamedCreature("greaterShadow")
        .build()

    fun connect(fromMap: GameMap, toMap: GameMap, fromChar: Char, toChar: Char) {
        val mutualStairs = fromMap.randomCommunalFloor(toMap)
        val (sx, sy) = mutualStairs
        fromMap.connect(mutualStairs, mutualStairs, toMap.id)
        fromMap.setTile(sx, sy, fromChar)
        toMap.connect(mutualStairs, mutualStairs, fromMap.id)
        toMap.setTile(sx, sy, toChar)
    }
}