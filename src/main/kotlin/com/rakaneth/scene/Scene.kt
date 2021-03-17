package com.rakaneth.scene

import com.rakaneth.GameConfig
import com.valkryst.VTerminal.component.VPanel

open class Scene(val sceneName: String): VPanel(GameConfig.GAME_W, GameConfig.GAME_H) {
    open fun redraw() {
        repaint()
    }
}