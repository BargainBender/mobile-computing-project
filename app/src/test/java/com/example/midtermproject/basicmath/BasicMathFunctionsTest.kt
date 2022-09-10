package com.example.midtermproject.basicmath

import org.junit.Assert.*

import org.junit.Test

class BasicMathFunctionsTest {
    @Test
    fun add_case1_isCorrect() {
        assertEquals(7, BasicMathFunctions.add(5,2))
    }

    @Test
    fun subtract_case1_isCorrect() {
        assertEquals(-5, BasicMathFunctions.subtract(4,9))
    }

    @Test
    fun multiply_case1_isCorrect() {
        assertEquals(42, BasicMathFunctions.multiply(21,2))
    }

    @Test
    fun divide_case1_isCorrect() {
        assertEquals(3, BasicMathFunctions.divide(27, 9))
    }
}