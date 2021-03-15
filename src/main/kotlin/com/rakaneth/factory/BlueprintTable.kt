package com.rakaneth.factory

interface BlueprintTable<T: Blueprint> {
    val table: Map<String, T>
}