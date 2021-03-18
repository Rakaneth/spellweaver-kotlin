package com.rakaneth

import com.rakaneth.engine.Messenger
import com.rakaneth.scene.SceneManager
import com.valkryst.VTerminal.palette.Palette
import com.valkryst.VTerminal.plaf.VTerminalLookAndFeel
import java.awt.Toolkit
import java.util.logging.LogManager
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val logStream = javaClass.classLoader.getResourceAsStream("logging.properties")
        logStream?.use {
            LogManager.getLogManager().readConfiguration(it)
        }

        val laf = VTerminalLookAndFeel.getInstance(GameConfig.FONT_SIZE)

        try {
            UIManager.setLookAndFeel(laf)
        } catch (e: UnsupportedLookAndFeelException) {
            e.printStackTrace()
        }

        val stream = Main.javaClass.classLoader.getResourceAsStream("Dracula.properties")
        stream?.use {
            Palette.loadAndRegisterProperties(it)
        }

        SceneManager.startUI()
        SceneManager.changeScene("play")

        Messenger.addMessage("This is a long message to see if the JTextPane will wrap properly.")
        Messenger.addMessage("Here is another long message that should help to test scrolling.")
        Messenger.addMessage("A third long message to hopefully fill the box and force it to scroll.")
        Messenger.addMessage("Still not scrolling, so let's ramble on some more. Do I get a scroll pane?")
        Messenger.addMessage("Okay, a fifth line surely should be enough. No one likes a rambler.")
    }
}