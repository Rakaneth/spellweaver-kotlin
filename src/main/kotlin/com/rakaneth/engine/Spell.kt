package com.rakaneth.engine

import com.rakaneth.extensions.ZERO_COORD
import squidpony.squidai.PointAOE
import squidpony.squidai.Technique
import squidpony.squidmath.Coord


class Spell(
    var origin: Coord = ZERO_COORD,
    var tiles: Array<CharArray>
) : Technique("spell", PointAOE(origin)) {
    init {
        setMap(tiles)
    }

    var center: Coord = ZERO_COORD
    val elements: MutableSet<GameElement> = mutableSetOf()
    var range: Int = 1
    var potency: Int = 1
    var radius: Int = 1


    fun charge(element: GameElement) {

    }


}