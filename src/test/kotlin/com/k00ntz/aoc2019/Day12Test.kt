package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.Point3
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12Test {

    val day12 = Day12()

    @Test
    fun part2() {
        val inputPos = listOf(
            Point3(-1, 0, 2, "1"),
            Point3(2, -10, -7, "2"),
            Point3(4, -8, 8, "3"),
            Point3(3, 5, -1, "4")
        )
        assertEquals(2772, day12.part2(inputPos))
        val input2 = listOf(
            Point3(-8, -10, 0, "1"),
            Point3(5, 5, 10, "2"),
            Point3(2, -7, 3, "3"),
            Point3(9, -8, -3, "4")
        )
        assertEquals(4686774924L, day12.part2(input2))
    }

    @Test
    fun quadHelper() {
        val quad = Day12.QuadHelper(-1, 2, 4, 3)
        assertEquals(Day12.QuadHelper(3, 1, -3, -1), quad.gravityQuad())
    }

    @Test
    fun step() {
        val inputPos = listOf(
            Point3(-1, 0, 2, "1") to Point3(0, 0, 0),
            Point3(2, -10, -7, "2") to Point3(0, 0, 0),
            Point3(4, -8, 8, "3") to Point3(0, 0, 0),
            Point3(3, 5, -1, "4") to Point3(0, 0, 0)
        )
        val oneStep = day12.step(inputPos)
        assertEquals(
            setOf(
                Point3(2, -1, 1, "1"),
                Point3(3, -7, -4, "2"),
                Point3(1, -7, 5, "3"),
                Point3(2, 2, 0, "4")
            ), oneStep.map { it.first }.toSet()
        )
        val twoStep = day12.step(oneStep)
        assertEquals(
            setOf(
                Point3(5, -3, -1, "1"),
                Point3(1, -2, 2, "2"),
                Point3(1, -4, -1, "3"),
                Point3(1, -4, 2, "4")
            ), twoStep.map { it.first }.toSet()
        )

    }

    @Test
    fun takeSteps() {
        val inputPos = listOf(
            Point3(-1, 0, 2, "1") to Point3(0, 0, 0),
            Point3(2, -10, -7, "2") to Point3(0, 0, 0),
            Point3(4, -8, 8, "3") to Point3(0, 0, 0),
            Point3(3, 5, -1, "4") to Point3(0, 0, 0)
        )
        val tenSteps = day12.takeSteps(inputPos, 10)
        assertEquals(
            setOf(
                Point3(2, 1, -3, "1"),
                Point3(1, -8, 0, "2"),
                Point3(3, -6, 1, "3"),
                Point3(2, 0, 4, "4")
            ), tenSteps.map { it.first }.toSet()
        )
    }

    @Test
    fun totalEnergy() {
        val inputPos = listOf(
            Point3(-1, 0, 2, "1") to Point3(0, 0, 0),
            Point3(2, -10, -7, "2") to Point3(0, 0, 0),
            Point3(4, -8, 8, "3") to Point3(0, 0, 0),
            Point3(3, 5, -1, "4") to Point3(0, 0, 0)
        )
        val tenSteps = day12.takeSteps(inputPos, 10)
        assertEquals(179, day12.totalEnergy(tenSteps))

        val input2 = listOf(
            Point3(-8, -10, 0, "1") to Point3(0, 0, 0),
            Point3(5, 5, 10, "2") to Point3(0, 0, 0),
            Point3(2, -7, 3, "3") to Point3(0, 0, 0),
            Point3(9, -8, -3, "4") to Point3(0, 0, 0)
        )

        val hundredSteps = day12.takeSteps(input2, 100)
        assertEquals(1940, day12.totalEnergy(hundredSteps))
    }

    @Test
    fun gravity() {
        assertEquals(
            Point3(-1, 1, 1),
            day12.gravity(Point3(2, 0, 0), Point3(1, 20, 200))
        )
    }
}