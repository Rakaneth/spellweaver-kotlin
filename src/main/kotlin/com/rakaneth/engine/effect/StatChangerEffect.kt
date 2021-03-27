package com.rakaneth.engine.effect

class StatChangerEffect(
    name: String,
    duration: Int,
    resistType: ResistType,
    val atk: Int = 0,
    val dmg: Int = 0,
    val dfp: Int = 0,
    val tou: Int = 0,
    val will: Int = 0,
    val spd: Int = 0
) : Effect(name, duration, resistType)