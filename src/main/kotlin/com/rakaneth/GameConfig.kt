package com.rakaneth

import com.rakaneth.engine.GameState
import com.rakaneth.factory.EntityFactory
import com.rakaneth.factory.MapFactory

object GameConfig {
    val GAME_W = 100
    val GAME_H = 40
    val MAP_W = 70
    val MAP_H = 30
    val STAT_W = 30
    val STAT_H = 30
    val INFO_W = 30
    val INFO_H = 10
    val SKIL_W = 30
    val SKIL_H = 10
    val MSG_W = 40
    val MSG_H = 10

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
    }
}