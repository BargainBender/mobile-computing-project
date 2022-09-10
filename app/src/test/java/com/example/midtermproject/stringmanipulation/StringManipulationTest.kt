package com.example.midtermproject.stringmanipulation

import org.junit.Assert.*

import org.junit.Test

class StringManipulationTest {
    @Test
    fun convertConsonantsToSign_isCorrect() {
        assertEquals("\$e\$a\$\$ia\$", StringManipulation.convertConsonantsToSign("Sebastian"))
    }

    @Test
    fun getUppercase_isCorrect() {
        assertEquals("SEBASTIAN", StringManipulation.getUppercase("Sebastian"))
    }

    @Test
    fun getVowelCount_isCorrect() {
        assertEquals(4, StringManipulation.getVowelCount("Sebastian"))
    }
}