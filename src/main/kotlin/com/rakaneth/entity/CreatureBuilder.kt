package com.rakaneth.entity

import com.rakaneth.Swatch
import com.rakaneth.entity.component.flags.PlayerComponent
import java.awt.Color

class CreatureBuilder {
    var name: String = "noname"
    var desc: String = "nodesc"
    var vision: Double = 6.0
    var glyph: Char = '@'
    var fg: Color = Color.WHITE
    var bg: Color = Swatch.TRANSPARENT

    fun withName(name: String) = also {
        it.name = name
    }

    fun withDesc(desc: String) = also {
        it.desc = desc
    }

    fun withVision(vision: Double) = also {
        it.vision = vision
    }

    fun withFG(fg: Color) = also {
        it.fg = fg
    }

    fun withBG(bg: Color) = also {
        it.bg = bg
    }

    fun withGlyph(glyph: Char) = also {
        it.glyph = glyph
    }

    fun buildCreature(): Entity {
        return Entity(glyph, name, desc, "nomap", fg, bg)
    }

    fun buildPlayer(): Entity {
        val newPlayer = buildCreature()
        newPlayer.addComponent(PlayerComponent())
        return newPlayer
    }

}