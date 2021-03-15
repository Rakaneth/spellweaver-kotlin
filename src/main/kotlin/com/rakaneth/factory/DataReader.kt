package com.rakaneth.factory

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.introspector.BeanAccess
import java.io.BufferedInputStream
import java.io.IOException

object DataReader {
    private const val creatureFile: String = "creatures.yml"
    private val logger: Logger = LoggerFactory.getLogger(DataReader::class.java)

    private fun <T: BlueprintTable<out Blueprint>> loadBlueprints(fileName: String, klass: Class<T>): T {
        val yaml = Yaml()
        yaml.setBeanAccess(BeanAccess.FIELD)
        var result: T? = null
        val stream = DataReader.javaClass.classLoader.getResourceAsStream(fileName)!!
        try {
            BufferedInputStream(stream).use {
                result = yaml.loadAs(it, klass)
                logger.info("${result?.table?.size} items loaded.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result!!
    }

    fun loadCreatures(): CreatureTable = loadBlueprints(creatureFile, CreatureTable::class.java)
}