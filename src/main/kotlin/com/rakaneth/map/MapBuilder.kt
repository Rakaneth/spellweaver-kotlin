package com.rakaneth.map

import com.rakaneth.Swatch
import com.rakaneth.engine.GameRNG
import com.rakaneth.factory.DataReader
import squidpony.squidgrid.mapping.DungeonGenerator
import squidpony.squidgrid.mapping.DungeonUtility
import squidpony.squidgrid.mapping.SerpentMapGenerator
import java.awt.Color

class MapBuilder(val width: Int, val height: Int) {
    private var id: String = "noid"
    private var name: String = "noname"
    private var light: Boolean = false
    private var wallColor: Color = Swatch.CAVE_WALL_BG
    private var floorColor: Color = Swatch.CAVE_FLOOR_BG
    private var maxCreatures: Int = 0
    private var waterPct: Int = 0
    private var doorPct: Int = 0
    private var caveCarvers: Int = 1
    private var roundCarvers: Int = 0
    private var boxCarvers: Int = 0
    private var doubleDoors: Boolean = false
    private val namedCreatures: MutableList<String> = mutableListOf()
    private val namedItems: MutableList<String> = mutableListOf()
    private val dgn: DungeonGenerator = DungeonGenerator(width, height, GameRNG.mapRNG)
    private val spt: SerpentMapGenerator = SerpentMapGenerator(width, height, GameRNG.mapRNG, 0.2)


    fun withID(id: String) = also { this.id = id }
    fun withName(name: String) = also { this.name = name }
    fun withLight(light: Boolean) = also { this.light = light }
    fun withWallColor(wallColor: Color) = also { this.wallColor = wallColor }
    fun withFloorColor(floorColor: Color) = also { this.floorColor = floorColor }
    fun withMaxCreatures(maxCreatures: Int) = also { this.maxCreatures = maxCreatures }
    fun withNamedCreature(creatureID: String) = also { namedCreatures.add(creatureID) }
    fun withNamedItem(itemID: String) = also { namedItems.add(itemID) }
    fun withWaterPct(waterPct: Int) = also { this.waterPct = waterPct }
    fun withDoorPct(doorPct: Int) = also { this.doorPct = doorPct }
    fun withDoubleDoors(doubleDoors: Boolean) = also { this.doubleDoors = doubleDoors }
    fun withBoxCarvers(carvers: Int) = also { this.boxCarvers = carvers }
    fun withCaveCarvers(carvers: Int) = also { this.caveCarvers = carvers }
    fun withRoundCarvers(carvers: Int) = also { this.roundCarvers = carvers }
    fun buildFromXPFile(fileName: String): GameMap {
        val tiles = DataReader.readXPFile(fileName)
        if (!(tiles.size == width && tiles[0].size == height)) {
            throw IllegalArgumentException("XP file dimensions must match map dimensions")
        }
        return GameMap(id, name, tiles, light, wallColor, floorColor)
    }

    fun build(): GameMap {
        spt.putBoxRoomCarvers(boxCarvers)
        spt.putCaveCarvers(caveCarvers)
        spt.putRoundRoomCarvers(roundCarvers)
        val baseTiles = spt.generate()

        dgn.addDoors(doorPct, doubleDoors)
        dgn.addWater(waterPct)
        val finalTiles = dgn.generate(baseTiles)

        DungeonUtility.closeDoors(finalTiles)

        //TODO: critters & item seeding
        return GameMap(id, name, DungeonUtility.closeDoors(finalTiles), light, wallColor, floorColor)
    }


}