package com.k00ntz.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day16Test {

    val day16 = Day16()

    @Test
    fun basePattern() {
        assertEquals(0, day16.getBasePatternForIndex(1, 0))
        assertEquals(1, day16.getBasePatternForIndex(0, 0))
        assertEquals(0, day16.getBasePatternForIndex(0, 1))
        assertEquals(-1, day16.getBasePatternForIndex(0, 2))
        assertEquals(0, day16.getBasePatternForIndex(0, 3))

//        assertEquals(listOf(1, 0, -1, 0), (0..3).map { day16.getBasePatternForIndex(0, it) })
    }

    @Test
    fun phase() {
        assertEquals(listOf(4, 8, 2, 2, 6, 1, 5, 8), day16.phase(listOf(1, 2, 3, 4, 5, 6, 7, 8)))
        assertEquals(listOf(3, 4, 0, 4, 0, 4, 3, 8), day16.phase(listOf(4, 8, 2, 2, 6, 1, 5, 8)))
        assertEquals(listOf(0, 3, 4, 1, 5, 5, 1, 8), day16.phase(listOf(3, 4, 0, 4, 0, 4, 3, 8)))
        assertEquals(listOf(0, 1, 0, 2, 9, 4, 9, 8), day16.phase(listOf(0, 3, 4, 1, 5, 5, 1, 8)))
    }

    @Test
    fun phaseBig() {
        assertEquals(
            "24176176",
            day16.phase("80871224585914546619083218645595".toCharArray().map { '0' - it }, 100).take(8).joinToString(
                separator = ""
            )
        )
        assertEquals(
            "73745418",
            day16.phase("19617804207202209144916044189917".toCharArray().map { '0' - it }, 100).take(8).joinToString(
                separator = ""
            )
        )
        assertEquals(
            "52432133",
            day16.phase("69317163492948606335995924319873".toCharArray().map { '0' - it }, 100).take(8).joinToString(
                separator = ""
            )
        )
    }
}