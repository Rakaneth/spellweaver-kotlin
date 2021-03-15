package com.rakaneth.factory

data class CreatureBlueprint(
    //TODO: change this as stuff gets reimplemented
    val glyph: Char = '@',
    val color: String = "0,0,0",
    val name: String = "noname",
    val desc: String = "nodesc",
    val hp: Int = 0,
    val will: Int = 0,
    val atk: Int = 0,
    val dmg: Int = 0,
    val dfp: Int = 0,
    val tou: Int = 0,
    val spd: Int = 0,
    val weakness: String = "NONE",
    val resistance: String = "NONE",
    val ai: String = "HUNT",
    val damType: String = "NONE",
    val level: Int = 0,
    override val freq: Int = 0): Blueprint