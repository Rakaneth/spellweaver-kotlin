package com.rakaneth

import com.rakaneth.scene.MapPanel
import com.rakaneth.scene.SceneManager
import com.valkryst.VTerminal.palette.Palette
import com.valkryst.VTerminal.plaf.VTerminalLookAndFeel
import java.awt.Toolkit
import java.io.FileInputStream
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.WindowConstants.EXIT_ON_CLOSE

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val dims = Toolkit.getDefaultToolkit().screenSize
        val height: Int = dims.height / 55

        val laf = VTerminalLookAndFeel.getInstance(height)
        try {
            UIManager.setLookAndFeel(laf)
        } catch (e: UnsupportedLookAndFeelException) {
            e.printStackTrace()
        }

        val stream = Main.javaClass.classLoader.getResourceAsStream("Dracula.properties")
        stream.use {
            Palette.loadAndRegisterProperties(it!!)
        }

        SceneManager.startUI()
        SceneManager.changeScene("play")
    }
}