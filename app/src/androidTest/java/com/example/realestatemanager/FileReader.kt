package com.example.realestatemanager

import java.io.InputStreamReader

object FileReader {

    private fun getJsonContent(fileName: String): String {
        return InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(fileName)).use { it.readText() }
    }
}