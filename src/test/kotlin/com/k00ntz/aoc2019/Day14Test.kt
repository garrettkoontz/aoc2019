package com.k00ntz.aoc2019

import org.junit.jupiter.api.Test

internal class Day14Test {

    val day14 = Day14()

    val input1String = "10 ORE => 10 A\n" +
            "1 ORE => 1 B\n" +
            "7 A, 1 B => 1 C\n" +
            "7 A, 1 C => 1 D\n" +
            "7 A, 1 D => 1 E\n" +
            "7 A, 1 E => 1 FUEL"
    val input1 = input1String.split("\n").map { day14.parseEq(it) }.toMap()

    @Test
    fun findBaseAmount() {
        val chemTree = day14.createChemTree(input1, "ORE", "FUEL")
//        val pair = day14.findBaseAmount(input1, mutableMapOf(), Pair(1, "FUEL"), "ORE")
//        assertEquals(31, pair.first)
        print(chemTree.sum())
    }
}