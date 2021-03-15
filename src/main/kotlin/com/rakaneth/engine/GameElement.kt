package com.rakaneth.engine

sealed class GameElement {
    data class Fire(
        val name: String = "fire",
        val baseCost: Int = 1,
        val secondCost: Int = 2,
        val thirdCost: Int = 2
    ) : GameElement()

    data class Ice(
        val name: String = "ice",
        val baseCost: Int = 1,
        val secondCost: Int = 2,
        val thirdCost: Int = 2
    ) : GameElement()

    data class Lightning(
        val name: String = "lightning",
        val baseCost: Int = 1,
        val secondCost: Int = 3,
        val thirdCost: Int = 1
    ) : GameElement()

    data class Earth(
        val name: String = "earth",
        val baseCost: Int = 1,
        val secondCost: Int = 1,
        val thirdCost: Int = 1
    ) : GameElement()

    data class Force(
        val name: String = "force",
        val baseCost: Int = 2,
        val secondCost: Int = 0,
        val thirdCost: Int = 1
    ) : GameElement()

    data class Light(
        val name: String = "light",
        val baseCost: Int = 3,
        val secondCost: Int = 3,
        val thirdCost: Int = 1
    ) : GameElement()

    data class Dark(
        val name: String = "darkness",
        val baseCost: Int = 3,
        val secondCost: Int = 2,
        val thirdCost: Int = 1
    ) : GameElement()

    data class Physical(val name: String = "physical") : GameElement()
    object None : GameElement()
}
