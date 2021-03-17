package com.rakaneth.scene

import com.rakaneth.GameConfig
import com.rakaneth.engine.GameState
import com.rakaneth.entity.Entity
import com.rakaneth.entity.component.CasterComponent
import com.rakaneth.entity.component.CombatantComponent
import com.rakaneth.entity.component.VitalsComponent
import com.rakaneth.extensions.*
import com.valkryst.VTerminal.component.VPanel
import org.hexworks.cobalt.databinding.api.binding.bindAndWith
import org.hexworks.cobalt.databinding.api.binding.bindTransform
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.internal.binding.ComputedBinding
import java.awt.Color
import java.awt.FlowLayout
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JLabel

class PlayScene(): Scene("play") {
    val map: MapPanel = MapPanel(GameConfig.MAP_W, GameConfig.MAP_H)
    val stats: VPanel = VPanel(GameConfig.STAT_W, GameConfig.STAT_H)
    val info: VPanel = VPanel(GameConfig.INFO_W, GameConfig.INFO_H)
    val skills: VPanel = VPanel(GameConfig.SKIL_W, GameConfig.SKIL_H)
    val msgs: VPanel = VPanel(GameConfig.MSG_W, GameConfig.MSG_H)
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

        add(map)
        add(stats)
        add(msgs)
        add(skills)
        add(info)

        GameState.redrawProp.onChange {
            if (it.newValue)
            {
                map.redraw()
                mapDone.updateValue(true)
            }

        }

        allDone.onChange {
            if (it.newValue) {
                GameState.redraw = false
                listOf(mapDone, magDone, infoDone, statsDone, msgDone).forEach { flag ->
                    flag.updateValue(false)
                }
            }
        }
    }

    private fun border(panel: VPanel, title: String) {
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
                redraw()
                GameState.redrawProp.value = false
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

    override fun redraw() {
        map.redraw()
        stats.reset()
        stats.repaint()
        super.redraw()
    }

    override fun setKeyBinds() {
        TODO("Not yet implemented")
    }
}