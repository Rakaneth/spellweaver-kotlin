package com.rakaneth.map

import com.rakaneth.Swatch.DEEP_BG
import com.rakaneth.Swatch.DOOR_BG
import com.rakaneth.Swatch.SHALLOW_BG
import com.rakaneth.Swatch.STAIR_FG
import com.rakaneth.engine.GameRNG
import org.hexworks.cobalt.datatypes.Maybe
import squidpony.squidai.DijkstraMap
import squidpony.squidgrid.Measurement
import squidpony.squidgrid.mapping.DungeonUtility
import squidpony.squidmath.Coord
import squidpony.squidmath.GreasedRegion
import java.awt.Color

class GameMap(
    val id: String,
    val name: String,
    val tiles: Array<CharArray>,
    var light: Boolean,
    val wallColor: Color,
    val floorColor: Color
) {
    data class Connection(val dest: Coord, val mapID: String)
    data class Tile(val glyph: Char, val walk: Boolean, val see: Boolean, val fg: Color, val bg: Color)

    val tileInfo: Map<Char, Tile> = mapOf(
        '#' to Tile(' ', walk = false, see = false, fg = Color.BLACK, bg = wallColor),
        '.' to Tile(' ', walk = true, see = true, fg = Color.BLACK, bg = floorColor),
        '~' to Tile(' ', walk = true, see = true, fg = Color.BLACK, bg = DEEP_BG),
        ',' to Tile(' ', walk = true, see = true, fg = Color.BLACK, bg = SHALLOW_BG),
        '+' to Tile('+', walk = false, see = false, fg = Color.WHITE, bg = DOOR_BG),
        '/' to Tile('/', walk = true, see = true, fg = Color.WHITE, bg = DOOR_BG),
        '>' to Tile('>', walk = true, see = true, fg = STAIR_FG, bg = floorColor),
        '<' to Tile('<', walk = true, see = true, fg = STAIR_FG, bg = floorColor)
    )

    private val floors: GreasedRegion = GreasedRegion(tiles, '.')
    private val temp: GreasedRegion = GreasedRegion(tiles, '.')
    private val explored: GreasedRegion = GreasedRegion(floors.width, floors.height)
    var resistances: Array<DoubleArray> = DungeonUtility.generateSimpleResistances(tiles)
    val connections: MutableMap<Coord, Connection> = mutableMapOf()
    val costs: Array<DoubleArray> = DungeonUtility.generateCostMap(
        tiles, mapOf(
            '~' to 3.0,
            ',' to 1.5
        ), 1.0
    )
    val dmap: DijkstraMap = DijkstraMap(costs, Measurement.MANHATTAN)
    val width: Int
        get() = tiles.size

    val height: Int
        get() = tiles[0].size

    fun isInBounds(x: Int, y: Int): Boolean = x in 0 until width && y in 0 until height

    fun tile(x: Int, y: Int): Maybe<Tile> {
        return if (isInBounds(x, y)) {
            Maybe.of(tileInfo[tiles[x][y]]!!)
        } else Maybe.empty()
    }

    fun cost(x: Int, y: Int): Double = costs[x][y]
    fun setTile(x: Int, y: Int, c: Char) {
        tiles[x][y] = c
        resistances = DungeonUtility.generateSimpleResistances(tiles)
        floors.refill(tiles, '.')
    }

    fun closeDoor(x: Int, y: Int) {
        setTile(x, y, '+')
    }

    fun openDoor(x: Int, y: Int) {
        setTile(x, y, '/')
    }

    fun connect(start: Coord, end: Coord, mapID: String) {
        connections.putIfAbsent(start, Connection(end, mapID))
    }

    fun updateExplored(visible: Array<DoubleArray>) {
        explored.or(temp.refill(visible, 0.0).not())
    }

    private fun tileCheck(x: Int, y: Int, fn: (Tile) -> Boolean): Boolean {
        return tile(x, y).fold(
            whenEmpty = { false },
            whenPresent = fn
        )
    }

    fun isBlocking(x: Int, y: Int) = tileCheck(x, y) { t -> !t.walk }
    fun isOpaque(x: Int, y: Int) = tileCheck(x, y) { t -> !t.see }
    fun isClosedDoor(x: Int, y: Int) = tileCheck(x, y) { t -> t.glyph == '+' }
    fun isStairs(x: Int, y: Int) = tileCheck(x, y) { t -> "<>".indexOf(t.glyph) > -1 }

    fun randomFloor(): Coord = floors.singleRandom(GameRNG.mapRNG)
    fun randomFloorAround(x: Int, y: Int, r: Int): Coord {
        return temp.empty()
            .set(true, Coord.get(x, y))
            .flood(floors, r)
            .singleRandom(GameRNG.mapRNG)
    }

    fun randomCommunalFloor(other: GameMap): Coord {
        return temp.remake(floors)
            .and(other.floors)
            .singleRandom(GameRNG.mapRNG)
    }

    fun rawTile(x: Int, y: Int): Char {
        return if (isInBounds(x, y)) tiles[x][y] else 0.toChar()
    }

    val visionMap: Array<DoubleArray> = Array(width) { DoubleArray(height) }

    companion object {
        fun newBuilder(width: Int, height: Int): MapBuilder = MapBuilder(width, height)
    }
}