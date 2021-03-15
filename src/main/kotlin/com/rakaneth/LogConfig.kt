package com.rakaneth

import java.util.logging.LogManager

class LogConfig {
    init {
        val stream = javaClass.classLoader.getResourceAsStream("logging.properties")
        LogManager.getLogManager().readConfiguration(stream)
        stream?.close()
    }
}