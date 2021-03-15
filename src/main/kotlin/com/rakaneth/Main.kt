package com.rakaneth

import com.rakaneth.engine.GameState
import com.rakaneth.scene.MapPanel
import com.valkryst.VTerminal.plaf.VTerminalLookAndFeel
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.WindowConstants.EXIT_ON_CLOSE

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        GameConfig.newGame()

        val laf = VTerminalLookAndFeel.getInstance(16)
        try {
            UIManager.setLookAndFeel(laf)
        } catch (e: UnsupportedLookAndFeelException) {
            e.printStackTrace()
        }

        val frame = JFrame("Spellweaver")
        val panel = MapPanel(GameConfig.MAP_W, GameConfig.MAP_H)
        frame.add(panel)
        frame.pack()
        frame.defaultCloseOperation = EXIT_ON_CLOSE
        frame.isVisible = true

        SwingUtilities.invokeLater {
            panel.redraw()
        }
    }
}