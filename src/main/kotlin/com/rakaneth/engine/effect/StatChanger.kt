package com.rakaneth.engine.effect

class StatChanger(
    name: String,
    duration: Int,
    isDebuff: Boolean,
    val atk: Int = 0,
    val dmg: Int = 0,
    val dfp: Int = 0,
    val tou: Int = 0,
    val will: Int = 0,
    val spd: Int = 0
) : Effect(name, duration, isDebuff)