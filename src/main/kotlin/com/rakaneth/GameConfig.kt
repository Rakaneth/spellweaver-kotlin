package com.rakaneth

import com.rakaneth.engine.GameState
import com.rakaneth.factory.EntityFactory
import com.rakaneth.factory.MapFactory
import java.awt.Toolkit

class GameConfig {

    fun newGame() {
        MapFactory.connect(MapFactory.firstFloor, MapFactory.secondFloor, '>', '<')
        MapFactory.connect(MapFactory.secondFloor, MapFactory.thirdFloor, '>', '<')
        MapFactory.connect(MapFactory.thirdFloor, MapFactory.fourthFloor, '>', '<')
        MapFactory.connect(MapFactory.fourthFloor, MapFactory.fifthFloor, '>', '<')
        listOf(
            MapFactory.firstFloor,
            MapFactory.secondFloor,
            MapFactory.thirdFloor,
            MapFactory.fourthFloor,
            MapFactory.fifthFloor
        ).forEach { map ->
            GameState.addMap(map)
        }

        GameState.addEntity(EntityFactory.newPlayer("Farin"))
        GameState.changeLevel("floor1")
        EntityFactory.randomSeed(GameState.player, GameState.curMap)
        GameState.update = true
    }

    companion object {
        const val GAME_W = 100
        const val GAME_H = 40
        const val MAP_W = 70
        const val MAP_H = 30
        const val STAT_W = 30
        const val STAT_H = 30
        const val INFO_W = 30
        const val INFO_H = 10
        const val SKIL_W = 30
        const val SKIL_H = 10
        const val MSG_W = 40
        const val MSG_H = 10
        val FONT_SIZE: Int  = Toolkit.getDefaultToolkit().screenSize.height / 55
        fun create(): GameConfig = GameConfig()
    }
}