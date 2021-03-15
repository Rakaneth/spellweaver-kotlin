package com.rakaneth.entity

import com.rakaneth.entity.component.Component
import org.hexworks.cobalt.datatypes.Maybe
import squidpony.squidmath.Coord
import squidpony.squidmath.SquidID
import java.awt.Color
import kotlin.reflect.KClass
import kotlin.reflect.cast

class Entity(val glyph: Char,
             val name: String,
             val desc: String,
             var mapID: String,
             val fg: Color,
             val bg: Color) {
    private val components: MutableList<Component> = mutableListOf()
    val id: String = SquidID().toString()
    var pos: Coord = Coord.get(0, 0)
    val x: Int
        get() = pos.x
    val y: Int
        get() = pos.y

    fun <T: Component> getComponent(klass: KClass<T>): Maybe<T> {
        val comp = components.filterIsInstance(klass.java).firstOrNull()
        return if (comp == null) {
            Maybe.empty()
        } else Maybe.of(klass.cast(comp))
    }

    fun <T: Component> whenHas(klass: KClass<T>, fn: (T) -> Unit) {
        getComponent(klass).ifPresent(fn)
    }

    fun addComponent(comp: Component) {
        components.add(comp)
    }

    fun addMany(vararg comps: Component) {
        comps.forEach { addComponent(it) }
    }

    fun removeComponent(name: String) {
        components.removeIf { comp -> comp.name == name }
    }

    companion object {
        fun newCreatureBuilder() = CreatureBuilder()
    }
}