package com.rakaneth.extensions

import com.valkryst.VTerminal.component.VPanel
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW
import javax.swing.KeyStroke
import javax.swing.KeyStroke.getKeyStroke

/**
 * Adds a binding for `keyCode` + `mods` to perform `fn`.
 * Assigns a label of `id` under the hood.
 * {@link KeyStroke#getKeyStroke(int, int, boolean)} for values of `mods`
 */
fun VPanel.addKeybind(keyCode: Int, mods: Int = 0, id: String, fn: (ActionEvent?) -> Unit) {
    val action = object : AbstractAction() {
        override fun actionPerformed(e: ActionEvent?) {
            fn(e)
        }
    }
    this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(getKeyStroke(keyCode, mods, false), id)
    actionMap.put(id, action)
}