package com.k00ntz.aoc2019

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class IntCodeComputerTest {

    @Test
    fun executePosEquals8() {
        val (_, output1) = IntCodeComputer(intArrayOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8)).executeProgram(input = 7)
        assertEquals(0, output1.first())
        val (_, output2) = IntCodeComputer(intArrayOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8)).executeProgram(input = 8)
        assertEquals(1, output2.first())
        val (_, output3) = IntCodeComputer(intArrayOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8)).executeProgram(input = 9)
        assertEquals(0, output3.first())
    }

    @Test
    fun executePosLT8() {
        val (_, output1) = IntCodeComputer(intArrayOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8)).executeProgram(input = 7)
        assertEquals(1, output1.first())
        val (_, output2) = IntCodeComputer(intArrayOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8)).executeProgram(input = 8)
        assertEquals(0, output2.first())
        val (_, output3) = IntCodeComputer(intArrayOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8)).executeProgram(input = 9)
        assertEquals(0, output3.first())
    }

    @Test
    fun executeImmEquals8() {
        val (_, output1) = IntCodeComputer(intArrayOf(3, 3, 1108, -1, 8, 3, 4, 3, 99)).executeProgram(input = 7)
        assertEquals(0, output1.first())
        val (_, output2) = IntCodeComputer(intArrayOf(3, 3, 1108, -1, 8, 3, 4, 3, 99)).executeProgram(input = 8)
        assertEquals(1, output2.first())
        val (_, output3) = IntCodeComputer(intArrayOf(3, 3, 1108, -1, 8, 3, 4, 3, 99)).executeProgram(input = 9)
        assertEquals(0, output3.first())
    }

    @Test
    fun executeImmLT8() {
        val (_, output1) = IntCodeComputer(intArrayOf(3, 3, 1107, -1, 8, 3, 4, 3, 99)).executeProgram(input = 7)
        assertEquals(1, output1.first())
        val (_, output2) = IntCodeComputer(intArrayOf(3, 3, 1107, -1, 8, 3, 4, 3, 99)).executeProgram(input = 8)
        assertEquals(0, output2.first())
        val (_, output3) = IntCodeComputer(intArrayOf(3, 3, 1107, -1, 8, 3, 4, 3, 99)).executeProgram(input = 9)
        assertEquals(0, output3.first())
    }

    @Test
    fun executeJmpPosTest0() {
        val (_, output1) = IntCodeComputer(
            intArrayOf(
                3,
                12,
                6,
                12,
                15,
                1,
                13,
                14,
                13,
                4,
                13,
                99,
                -1,
                0,
                1,
                9
            )
        ).executeProgram(input = -1)
        assertEquals(1, output1.first())
        val (_, output2) = IntCodeComputer(
            intArrayOf(
                3,
                12,
                6,
                12,
                15,
                1,
                13,
                14,
                13,
                4,
                13,
                99,
                -1,
                0,
                1,
                9
            )
        ).executeProgram(input = 0)
        assertEquals(0, output2.first())
        val (_, output3) = IntCodeComputer(
            intArrayOf(
                3,
                12,
                6,
                12,
                15,
                1,
                13,
                14,
                13,
                4,
                13,
                99,
                -1,
                0,
                1,
                9
            )
        ).executeProgram(input = 1)
        assertEquals(1, output3.first())
    }

    @Test
    fun executeJmpImmTest0() {
        val (_, output1) = IntCodeComputer(intArrayOf(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1)).executeProgram(
            input = -1
        )
        assertEquals(1, output1.first())
        val (_, output2) = IntCodeComputer(intArrayOf(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1)).executeProgram(
            input = 0
        )
        assertEquals(0, output2.first())
        val (_, output3) = IntCodeComputer(intArrayOf(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1)).executeProgram(
            input = 1
        )
        assertEquals(1, output3.first())
    }

    @Test
    fun executeGTEQLT8Test() {
        val gtEqLt8 = IntCodeComputer(
            intArrayOf(
                3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
            )
        )
        val (_, output1) = gtEqLt8.executeProgram(
            input = 7
        )
        assertEquals(999, output1.first())
        val (_, output2) = gtEqLt8.executeProgram(
            input = 8
        )
        assertEquals(1000, output2.first())
        val (_, output3) = gtEqLt8.executeProgram(
            input = 9
        )
        assertEquals(1001, output3.first())
    }

    @Test
    fun executeProgram() {
        assertArrayEquals(
            intArrayOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50),
            IntCodeComputer(intArrayOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)).executeProgram().first
        )
        assertArrayEquals(
            intArrayOf(2, 0, 0, 0, 99),
            IntCodeComputer(intArrayOf(1, 0, 0, 0, 99)).executeProgram().first
        )
        assertArrayEquals(
            intArrayOf(2, 3, 0, 6, 99),
            IntCodeComputer(intArrayOf(2, 3, 0, 3, 99)).executeProgram().first
        )
        assertArrayEquals(
            intArrayOf(2, 4, 4, 5, 99, 9801),
            IntCodeComputer(intArrayOf(2, 4, 4, 5, 99, 0)).executeProgram().first
        )
        assertArrayEquals(
            intArrayOf(30, 1, 1, 4, 2, 5, 6, 0, 99),
            IntCodeComputer(intArrayOf(1, 1, 1, 4, 99, 5, 6, 0, 99)).executeProgram().first
        )
    }

    @Test
    fun executeInstruction() {
        val intArray = intArrayOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)
        val output = Output()
        assertEquals(4, intArray.executeInstruction(Add(9, Mode.POSITION, 10, Mode.POSITION, 3), 0, null, output))
        assertArrayEquals(intArrayOf(1, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50), intArray)
        assertEquals(8, intArray.executeInstruction(Mul(3, Mode.POSITION, 11, Mode.POSITION, 0), 4, null, output))
        assertArrayEquals(intArrayOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50), intArray)
        assertEquals(9, intArray.executeInstruction(HLT(), 8, null, output))
        assertArrayEquals(intArrayOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50), intArray)
    }

}