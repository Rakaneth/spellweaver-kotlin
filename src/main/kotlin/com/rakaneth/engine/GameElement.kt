package com.rakaneth.engine

sealed class GameElement(val name: String) {
    data class Fire(
        val baseCost: Int = 1,
        val secondCost: Int = 2,
        val thirdCost: Int = 2
    ) : GameElement("fire")

    data class Ice(
        val baseCost: Int = 1,
        val secondCost: Int = 2,
        val thirdCost: Int = 2
    ) : GameElement("ice")

    data class Lightning(
        val baseCost: Int = 1,
        val secondCost: Int = 3,
        val thirdCost: Int = 1
    ) : GameElement("lightning")

    data class Earth(
        val baseCost: Int = 1,
        val secondCost: Int = 1,
        val thirdCost: Int = 1
    ) : GameElement("earth")

    data class Force(
        val baseCost: Int = 2,
        val secondCost: Int = 0,
        val thirdCost: Int = 1
    ) : GameElement("force")

    data class Light(
        val baseCost: Int = 3,
        val secondCost: Int = 3,
        val thirdCost: Int = 1
    ) : GameElement("light")

    data class Dark(
        val baseCost: Int = 3,
        val secondCost: Int = 2,
        val thirdCost: Int = 1
    ) : GameElement("darkness")

    object Physical : GameElement("physical")
    object None : GameElement("none")
}
