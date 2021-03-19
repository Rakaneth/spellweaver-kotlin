package com.rakaneth.scene

import com.rakaneth.GameConfig
import com.rakaneth.engine.GameState
import com.rakaneth.engine.Messenger
import com.rakaneth.entity.Entity
import com.rakaneth.entity.component.CasterComponent
import com.rakaneth.entity.component.CombatantComponent
import com.rakaneth.entity.component.VitalsComponent
import com.rakaneth.extensions.*
import com.valkryst.VTerminal.component.VPanel
import com.valkryst.VTerminal.component.VTextArea
import com.valkryst.VTerminal.component.VTextPane
import com.valkryst.VTerminal.plaf.VTerminalLookAndFeel
import org.hexworks.cobalt.databinding.api.binding.bindAndWith
import org.hexworks.cobalt.databinding.api.binding.bindTransform
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.internal.binding.ComputedBinding
import squidpony.squidgrid.Direction
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

class PlayScene: Scene("play") {
    val map: MapPanel = MapPanel(GameConfig.MAP_W, GameConfig.MAP_H)
    val stats: VPanel = VPanel(GameConfig.STAT_W, GameConfig.STAT_H)
    val info: VPanel = VPanel(GameConfig.INFO_W, GameConfig.INFO_H)
    val skills: VPanel = VPanel(GameConfig.SKIL_W, GameConfig.SKIL_H)
    val msgs: VPanel =  VPanel(GameConfig.MSG_W, GameConfig.MSG_H)
    val player: Entity
        get() = GameState.player

    private val mapDone = createPropertyFrom(false)
    private val statsDone = createPropertyFrom(false)
    private val infoDone = createPropertyFrom(false)
    private val msgDone = createPropertyFrom(false)
    private val magDone = createPropertyFrom(false)

    private val allDone = mapDone.bindAndWith(statsDone)
        .bindAndWith(infoDone)
        .bindAndWith(msgDone)
        .bindAndWith(magDone)

    init {
        layout = FlowLayout(FlowLayout.LEADING, 0, 0)

        border(stats, "Stats")
        border(msgs, "Messages")
        border(skills, "Magic")
        border(info, "info")

        createStats()
        createMsgs()
        createSkills()
        createInfo()

        add(map)
        add(stats)
        add(msgs)
        add(skills)
        add(info)

        GameState.redrawProp.onChange {
            if (it.newValue)
            {
                mapDone.updateValue(true)
            }
        }

        allDone.onChange {
            if (it.newValue) {
                SwingUtilities.invokeLater {
                    map.redraw()
                    redrawPanel(stats)
                    redrawPanel(skills)
                    redrawPanel(info)
                    GameState.redraw = false
                    listOf(mapDone, magDone, infoDone, statsDone, msgDone).forEach { flag ->
                        flag.updateValue(false)
                    }
                }
            }
        }

        addKeybind(KeyEvent.VK_W, id = "up") {
            GameState.player.moveBy(Direction.UP)
            GameState.redraw = true
        }
        addKeybind(KeyEvent.VK_S, id = "down") {
            GameState.player.moveBy(Direction.DOWN)
            GameState.redraw = true
        }
        addKeybind(KeyEvent.VK_A, id = "left") {
            GameState.player.moveBy(Direction.LEFT)
            GameState.redraw = true
        }
        addKeybind(KeyEvent.VK_D, id = "right") {
            GameState.player.moveBy(Direction.RIGHT)
            GameState.redraw = true
        }

        map.addMouseListener(object: MouseAdapter() {
            override fun mouseReleased(e: MouseEvent?) {
                val laf = UIManager.getLookAndFeel() as VTerminalLookAndFeel
                val x = e?.x?.div(laf.tileWidth) ?: 0
                val y = e?.y?.div(laf.tileHeight) ?: 0

                map.setCodePointAt(x, y, 'X'.toInt())
                map.repaint()
            }
        })
    }

    private fun border(panel: JComponent, title: String) {
        val lb = BorderFactory.createLineBorder(Color.WHITE)
        panel.border = BorderFactory.createTitledBorder(lb, title)
    }

    private fun createStats() {
        val bl = BoxLayout(stats, BoxLayout.Y_AXIS)
        stats.layout = bl
        val vitals = player.getComponent(VitalsComponent::class).get()
        val power = player.getComponent(CasterComponent::class).get()

        val hpLabel = JLabel(vitals.hpStringProp.value)
        val pwrLabel = JLabel(power.pwrStringProp.value)
        val atkLabel = JLabel()
        val dmgLabel = JLabel()
        val dfpLabel = JLabel()
        val touLabel = JLabel()
        val spdLabel = JLabel()
        val willLabel = JLabel()

        GameState.redrawProp.onChange {
            if (it.newValue) {
                atkLabel.text = "Atk: ${player.atk}"
                dmgLabel.text = "Dmg: ${player.dmg}"
                dfpLabel.text = "Dfp: ${player.dfp}"
                touLabel.text = "Tou: ${player.tou}"
                spdLabel.text = "Spd: ${player.spd}"
                willLabel.text = "Will: ${player.will}"
                statsDone.updateValue(true)
            }
        }

        stats.add(hpLabel)
        stats.add(pwrLabel)
        stats.add(atkLabel)
        stats.add(dmgLabel)
        stats.add(dfpLabel)
        stats.add(touLabel)
        stats.add(spdLabel)
        stats.add(willLabel)
    }

    private fun createMsgs() {
        val pane = VTextArea(GameConfig.MSG_H - 2, GameConfig.MSG_W - 2)
        val scroll = JScrollPane(pane)

        pane.rows = 100
        scroll.preferredSize = Dimension(22*GameConfig.FONT_SIZE, 10*GameConfig.FONT_SIZE)
        pane.lineWrap = true
        pane.wrapStyleWord = true
        pane.isEditable = false
        GameState.redrawProp.onChange {
            if (it.newValue) {
                Messenger.messages.reversed().forEach { msg ->
                    SwingUtilities.invokeLater {
                        pane.append("- $msg\n")
                        pane.caretPosition = 0
                    }
                }
                msgDone.updateValue(true)
            }
        }
        scroll.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        scroll.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        msgs.add(scroll, BorderLayout.CENTER)
    }

    private fun createSkills() {
        GameState.redrawProp.onChange {
            magDone.updateValue(true)
        }
    }

    private fun createInfo() {
        GameState.redrawProp.onChange {
            infoDone.updateValue(true)
        }
    }

    private fun redrawPanel(panel: VPanel) {
        panel.reset()
        panel.repaint()
    }
}