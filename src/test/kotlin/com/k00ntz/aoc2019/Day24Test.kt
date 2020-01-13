package com.k00ntz.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.streams.toList

internal class Day24Test {

    val day24 = Day24()

    val input1Str = "....#\n" +
            "#..#.\n" +
            "#..##\n" +
            "..#..\n" +
            "#...."
    val input1: List<List<Int>> = input1Str.split("\n").map {
        it.chars().mapToObj { it == '#'.toInt() }.map { if (it) 1 else 0 }.toList()
    }

    @Test
    fun part1() {
        assertEquals(2129920, day24.part1(input1))
    }

    @Test
    fun part2() {
        val outputMap = (0 until 10).fold(BugMap(input1)) { acc: BugMap, idx: Int ->
            acc.nextMap().also {
                println("$idx:")
                println(
                    it.map.entries.joinToString(
                        prefix = "\n",
                        separator = "\n"
                    ) { "${it.key}\n${it.value.asString()}" })
            }
        }
        assertEquals(99, outputMap.count())
    }
}