package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.Point3
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12Test {

    val day12 = Day12()

    @Test
    fun step() {
        val inputPos = listOf(
            Point3(-1, 0, 2) to Point3(0, 0, 0),
            Point3(2, -10, -7) to Point3(0, 0, 0),
            Point3(4, -8, 8) to Point3(0, 0, 0),
            Point3(3, 5, -1) to Point3(0, 0, 0)
        )
        val oneStep = day12.step(inputPos)
        assertEquals(setOf(Point3(2, -1, 1), Point3(3, -7, -4)), oneStep.map { it.first }.toSet())
    }

    @Test
    fun gravity() {
        assertEquals(
            Point3(1, -1, -1),
            day12.gravity(Point3(2, 0, 0), Point3(1, 2, 2))
        )
    }
}