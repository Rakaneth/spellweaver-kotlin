package com.rakaneth.engine

enum class GameElement(val eleName: String, val baseCost: Int, val secondCost: Int, val thirdCost: Int) {
    Fire("fire", 1, 1, 2),
    Ice("ice", 1, 2, 2),
    Lightning("lightning", 1, 3, 1),
    Earth("earth", 1, 1, 1),
    Force("force", 1, 1, 0),
    Light("light", 3, 3, 1),
    Dark("darkness", 3, 2, 1),
    Physical("physical", 0, 0, 0),
    None("none", 0, 0, 0);

    val opposite: GameElement
        get() = when (this) {
            Fire -> Ice
            Ice -> Fire
            Lightning -> Earth
            Earth -> Lightning
            Force -> Physical
            Light -> Dark
            Dark -> Light
            Physical -> Force
            None -> None
        }
}