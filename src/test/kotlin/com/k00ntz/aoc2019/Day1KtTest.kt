package com.k00ntz.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day1KtTest {
    val day1 = Day1()

    @Test
    fun moduleFuelCalc() {
        assertEquals(2, day1.moduleFuelCalc(12))
        assertEquals(2, day1.moduleFuelCalc(14))
        assertEquals(654, day1.moduleFuelCalc(1969))
        assertEquals(33583, day1.moduleFuelCalc(100756))
    }

    @Test
    fun moduleFuelCalcWithFuel(){
        assertEquals(2, day1.moduleFuelCalcWithFuel(12))
        assertEquals(2, day1.moduleFuelCalcWithFuel(14))
        assertEquals(966, day1.moduleFuelCalcWithFuel(1969))
        assertEquals(50346, day1.moduleFuelCalcWithFuel(100756))
    }
}