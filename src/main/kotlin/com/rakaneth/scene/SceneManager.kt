package com.rakaneth.scene

import com.rakaneth.GameConfig
import com.rakaneth.GameConfig.Companion.GAME_H
import com.rakaneth.GameConfig.Companion.GAME_W
import com.rakaneth.engine.GameState
import com.rakaneth.map.GameMap
import com.valkryst.VTerminal.component.VPanel
import java.awt.CardLayout
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

object SceneManager {

    private val cl = CardLayout()
    private val frame = JFrame("Spellweaver")
    private val panel = VPanel(GAME_W, GAME_H)

    fun startUI () {
        val gc = GameConfig()
        gc.newGame()
        panel.layout = cl
        panel.isFocusable = true

        addScene(PlayScene())
        /*
        panel.add(titlePanel, "title")
        panel.add(winPanel, "win")
        panel.add(losePanel, "lose")
        */
        frame.add(panel)
        frame.pack()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.isVisible = true
        frame.isResizable = false
    }

    private fun addScene(scene: Scene) {
        panel.add(scene, scene.sceneName)
    }

    fun changeScene(scene: String) {
        SwingUtilities.invokeLater {
            cl.show(panel, scene)
            GameState.redraw = true
        }
    }
}