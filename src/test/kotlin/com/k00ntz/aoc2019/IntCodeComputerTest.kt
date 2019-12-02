package com.k00ntz.aoc2019

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class IntCodeComputerTest {

    @Test
    fun executeProgram() {
        assertArrayEquals(
            intArrayOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50),
            IntCodeComputer(intArrayOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)).executeProgram()
        )
        assertArrayEquals(
            intArrayOf(2, 0, 0, 0, 99),
            IntCodeComputer(intArrayOf(1, 0, 0, 0, 99)).executeProgram()
        )
        assertArrayEquals(
            intArrayOf(2, 3, 0, 6, 99),
            IntCodeComputer(intArrayOf(2, 3, 0, 3, 99)).executeProgram()
        )
        assertArrayEquals(
            intArrayOf(2, 4, 4, 5, 99, 9801),
            IntCodeComputer(intArrayOf(2, 4, 4, 5, 99, 0)).executeProgram()
        )
        assertArrayEquals(
            intArrayOf(30, 1, 1, 4, 2, 5, 6, 0, 99),
            IntCodeComputer(intArrayOf(1, 1, 1, 4, 99, 5, 6, 0, 99)).executeProgram()
        )
    }

    @Test
    fun executeInstruction() {
        val intArray = intArrayOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)
        assertEquals(4, intArray.executeInstruction(Add(9, 10, 3), 0))
        assertArrayEquals(intArrayOf(1, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50), intArray)
        assertEquals(8, intArray.executeInstruction(Mul(3, 11, 0), 4))
        assertArrayEquals(intArrayOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50), intArray)
        assertEquals(9, intArray.executeInstruction(HLT(), 8))
        assertArrayEquals(intArrayOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50), intArray)
    }
}