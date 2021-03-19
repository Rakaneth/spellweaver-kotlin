package com.rakaneth.scene

import com.rakaneth.Swatch
import com.rakaneth.engine.GameState
import com.rakaneth.entity.Entity
import com.rakaneth.extensions.canSee
import com.rakaneth.extensions.component1
import com.rakaneth.extensions.component2
import com.rakaneth.map.GameMap
import com.valkryst.VTerminal.component.VPanel
import com.valkryst.VTerminal.plaf.VTerminalLookAndFeel
import squidpony.squidmath.Coord
import squidpony.squidmath.MathExtras
import java.awt.Color
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import javax.swing.UIManager
import kotlin.math.max

class MapPanel(width: Int, height: Int) : VPanel(width, height) {
    private val map: GameMap
        get() = GameState.curMap

    private val center: Entity
        get() = GameState.player

    private fun calc(p: Int, m: Int, s: Int) = MathExtras.clamp(p - s / 2, 0, max(0, m - s))
    private val cam: Coord
        get() = Coord.get(
            calc(center.x, map.width, this.widthInTiles),
            calc(center.y, map.height, this.heightInTiles)
        )

    private fun mapToScreen(mx: Int, my: Int): Coord {
        val mp = Coord.get(mx, my)
        val (cx, cy) = cam
        return mp.translate(-cx, -cy)
    }

    private fun screenToMap(sx: Int, sy: Int): Coord {
        val sp = Coord.get(sx, sy)
        val (cx, cy) = cam
        return sp.translate(cx, cy)
    }

    fun onMouseClick(fn: (Int, Int) -> Unit) {
        addMouseListener(object: MouseAdapter() {
            override fun mouseReleased(evt: MouseEvent?) {
                val laf = UIManager.getLookAndFeel() as VTerminalLookAndFeel
                val sx = evt?.x?.div(laf.tileWidth) ?: 0
                val sy = evt?.y?.div(laf.tileHeight) ?: 0
                val (mx, my) = screenToMap(sx, sy)
                fn(mx, my)
            }
        })
    }



    private fun inView(mx: Int, my: Int): Boolean {
        val (sx, sy) = mapToScreen(mx, my)
        return sx in 0 until widthInTiles && sy in 0 until heightInTiles
    }

    fun drawAtPoint(mx: Int, my: Int, glyph: Char, fg: Color, bg: Color) {
        if (inView(mx, my)) {
            val (sx, sy) = mapToScreen(mx, my)
            setCodePointAt(sx, sy, glyph.toInt())
            setBackgroundAt(sx, sy, bg)
            setForegroundAt(sx, sy, fg)
        }
    }

    fun updateCells(entities: List<Entity>) {
        val (cx, cy) = cam
        for (mx in cx until cx + widthInTiles) {
            for (my in cy until cy + heightInTiles) {
                map.tile(mx, my).ifPresent { tile ->
                    val shouldDraw = center.canSee(mx, my) || map.light
                    if (shouldDraw) {
                        drawAtPoint(mx, my, tile.glyph, tile.fg, tile.bg)
                    }
                }
            }
        }
        entities.forEach { entity ->
            val bg = if (entity.bg == Swatch.TRANSPARENT) map.floorColor else entity.bg
            if (center.canSee(entity.x, entity.y) || map.light) {
                drawAtPoint(entity.x, entity.y, entity.glyph, entity.fg, bg)
            }
        }
    }

    fun redraw() {
        reset()
        updateCells(GameState.currentEntities)
        repaint()
    }

}