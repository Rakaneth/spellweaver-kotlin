package com.rakaneth.factory

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.introspector.BeanAccess
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.GZIPInputStream

object DataReader {
    private const val creatureFile: String = "creatures.yml"
    private val logger: Logger = LoggerFactory.getLogger(DataReader::class.java)

    private fun <T : BlueprintTable<out Blueprint>> loadBlueprints(fileName: String, klass: Class<T>): T {
        val yaml = Yaml()
        yaml.setBeanAccess(BeanAccess.FIELD)
        var result: T? = null
        val stream = DataReader.javaClass.classLoader.getResourceAsStream(fileName)!!
        try {
            BufferedInputStream(stream).use {
                result = yaml.loadAs(it, klass)
                logger.info("${result?.table?.size} items loaded from $fileName.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result!!
    }

    fun loadCreatures(): CreatureTable = loadBlueprints(creatureFile, CreatureTable::class.java)

    fun readXPFile(fileName: String): Array<CharArray> {
        val stream = DataReader.javaClass.classLoader.getResourceAsStream(fileName)!!
        var tiles: Array<CharArray> = Array(1) { CharArray(1) }
        try {
            GZIPInputStream(stream).use {
                val bytes = it.readBytes()
                val bb = ByteBuffer.wrap(bytes)
                bb.order(ByteOrder.LITTLE_ENDIAN)
                val versionNumber = bb.getInt()
                logger.info("XP File version $versionNumber")
                val layers = bb.getInt()
                logger.info("$layers layers in this XP file")
                if (layers > 1) {
                    logger.info("More than one layer detected, only using first layer")
                }
                val width = bb.getInt()
                val height = bb.getInt()
                logger.info("$width by $height")
                tiles = Array(width) { CharArray(height) }
                for (idx in 0 until (width * height)) {
                    val x: Int = idx / height
                    val y: Int = idx % height
                    val c = bb.getInt()

                    val fr = bb.get()
                    val fg = bb.get()
                    val fb = bb.get()

                    val br = bb.get()
                    val bg = bb.get()
                    val Bb = bb.get()

                    tiles[x][y] = when (c) {
                        32 -> '.'
                        35 -> '#'
                        43 -> '+'
                        else -> '.'
                    }
                }
            }
        }  catch (ex: IOException) {
            ex.printStackTrace()
        }
        return tiles
    }
}