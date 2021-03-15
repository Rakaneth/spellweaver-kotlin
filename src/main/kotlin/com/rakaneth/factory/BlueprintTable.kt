package com.rakaneth.factory

import com.rakaneth.engine.GameRNG
import squidpony.squidmath.ProbabilityTable

interface BlueprintTable<T : Blueprint> {
    val table: Map<String, T>

    fun createProbabilityTable(fn: (T) -> Boolean): ProbabilityTable<T> {
        val pt = ProbabilityTable<T>(GameRNG.mapRNG)
        table.values.forEach { bp ->
            if (fn(bp)) {
                pt.add(bp, bp.freq)
            }
        }

        return pt
    }
}