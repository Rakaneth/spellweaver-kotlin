package com.rakaneth.scene

import com.rakaneth.GameConfig
import com.valkryst.VTerminal.component.VPanel
import java.awt.Component
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

open class Scene(val sceneName: String): VPanel(GameConfig.GAME_W, GameConfig.GAME_H) {
    init {
        isFocusable = true
        setKeyBinds()
    }

    open fun redraw() {
        repaint()
    }

    private fun setKeyBinds() {
        addComponentListener( object: ComponentAdapter() {
            override fun componentShown(e: ComponentEvent?) {
                val src = e?.source as Component
                src.requestFocus()
                src.requestFocusInWindow()
            }
        })
    }
}