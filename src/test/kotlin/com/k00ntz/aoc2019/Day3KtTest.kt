package com.k00ntz.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day3KtTest {

    @Test
    fun traceWire() {
        assertEquals(22, traceWire(listOf("R8", "U5", "L5", "D3").map { ManhattanDirection(it) }).size)
        assertEquals(22, traceWire(listOf("U7", "R6", "D4", "L4").map { ManhattanDirection(it) }).size)
    }

    @Test
    fun findClosestIntersection() {
        assertEquals(
            6,
            findClosestIntersection(listOf("R8", "U5", "L5", "D3").map { ManhattanDirection(it) },
                listOf("U7", "R6", "D4", "L4").map { ManhattanDirection(it) })
        )
        assertEquals(
            159,
            findClosestIntersection(
                listOf(
                    "R75", "D30", "R83", "U83", "L12", "D49", "R71", "U7", "L72"
                ).map { ManhattanDirection(it) },
                listOf("U62", "R66", "U55", "R34", "D71", "R55", "D58", "R83").map { ManhattanDirection(it) })
        )
        assertEquals(
            135,
            findClosestIntersection(listOf(
                "R98",
                "U47",
                "R26",
                "D63",
                "R33",
                "U87",
                "L62",
                "D20",
                "R33",
                "U53",
                "R51"
            ).map { ManhattanDirection(it) },
                listOf(
                    "U98",
                    "R91",
                    "D20",
                    "R16",
                    "D67",
                    "R40",
                    "U7",
                    "R15",
                    "U6",
                    "R7"
                ).map { ManhattanDirection(it) })
        )
    }

    @Test
    fun findFirstIntersection() {
        assertEquals(
            30,
            findFirstIntersection(listOf("R8", "U5", "L5", "D3").map { ManhattanDirection(it) },
                listOf("U7", "R6", "D4", "L4").map { ManhattanDirection(it) })
        )
        assertEquals(
            610,
            findFirstIntersection(
                listOf(
                    "R75", "D30", "R83", "U83", "L12", "D49", "R71", "U7", "L72"
                ).map { ManhattanDirection(it) },
                listOf("U62", "R66", "U55", "R34", "D71", "R55", "D58", "R83").map { ManhattanDirection(it) })
        )
        assertEquals(
            410,
            findFirstIntersection(listOf(
                "R98",
                "U47",
                "R26",
                "D63",
                "R33",
                "U87",
                "L62",
                "D20",
                "R33",
                "U53",
                "R51"
            ).map { ManhattanDirection(it) },
                listOf(
                    "U98",
                    "R91",
                    "D20",
                    "R16",
                    "D67",
                    "R40",
                    "U7",
                    "R15",
                    "U6",
                    "R7"
                ).map { ManhattanDirection(it) })
        )
    }
}