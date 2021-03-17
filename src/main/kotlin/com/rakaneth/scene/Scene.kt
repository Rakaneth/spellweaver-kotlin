package com.rakaneth.scene

import com.rakaneth.GameConfig
import com.valkryst.VTerminal.component.VPanel

abstract class Scene(val sceneName: String): VPanel(GameConfig.GAME_W, GameConfig.GAME_H) {
    open fun redraw() {
        repaint()
    }

    abstract fun setKeyBinds()
}