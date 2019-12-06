package com.k00ntz.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day6Test {

    val day6 = Day6()

    @Test
    fun buildGraph() {

    }

    @Test
    fun countOrbits() {
        val (graph, com) = day6.buildGraph(
            listOf(
                Pair("COM", "B"), Pair("B", "C"), Pair("C", "D"), Pair("D", "E"), Pair("E", "F"), Pair("B", "G"),
                Pair("G", "H"), Pair("D", "I"), Pair("E", "J"), Pair("J", "K"), Pair("K", "L")
            ))
        assertEquals(42, day6.countOrbits(com))
    }

    @Test
    fun minTransfers(){
        val (graph, com) = day6.buildGraph(
            listOf(
                Pair("COM", "B"), Pair("B", "C"), Pair("C", "D"), Pair("D", "E"), Pair("E", "F"), Pair("B", "G"),
                Pair("G", "H"), Pair("D", "I"), Pair("E", "J"), Pair("J", "K"), Pair("K", "L"), Pair("K", "YOU"), Pair("I", "SAN")
            ))
        assertEquals(4, day6.minTransfers(graph.findNode("YOU")!!, graph.findNode("SAN")!!))
    }
}