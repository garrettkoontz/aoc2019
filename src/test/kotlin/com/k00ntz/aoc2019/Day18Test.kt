package com.k00ntz.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day18Test {

    val day18 = Day18()

    val input1String = "#########\n" +
            "#b.A.@.a#\n" +
            "#########"
    val input1 = input1String.split("\n").map { it.toCharArray() }

    val input2String = "########################\n" +
            "#f.D.E.e.C.b.A.@.a.B.c.#\n" +
            "######################.#\n" +
            "#d.....................#\n" +
            "########################"
    val input2 = input2String.split("\n").map { it.toCharArray() }

    val input3String = "########################\n" +
            "#...............b.C.D.f#\n" +
            "#.######################\n" +
            "#.....@.a.B.c.d.A.e.F.g#\n" +
            "########################"
    val input4String = "#################\n" +
            "#i.G..c...e..H.p#\n" +
            "########.########\n" +
            "#j.A..b...f..D.o#\n" +
            "########@########\n" +
            "#k.E..a...g..B.n#\n" +
            "########.########\n" +
            "#l.F..d...h..C.m#\n" +
            "#################"
    val input5String = "########################\n" +
            "#@..............ac.GI.b#\n" +
            "###d#e#f################\n" +
            "###A#B#C################\n" +
            "###g#h#i################\n" +
            "########################"

    @Test
    fun findBestPath() {
        assertEquals(8, day18.part1(input1))
        assertEquals(86, day18.part1(input2))
        assertEquals(132, day18.part1(input3String.split("\n").map { it.toCharArray() }))
        assertEquals(136, day18.part1(input4String.split("\n").map { it.toCharArray() }))
        assertEquals(81, day18.part1(input5String.split("\n").map { it.toCharArray() }))
    }

    val input6String = "#######\n" +
            "#a.#Cd#\n" +
            "##...##\n" +
            "##.@.##\n" +
            "##...##\n" +
            "#cB#Ab#\n" +
            "#######"

    val input7String = "###############\n" +
            "#d.ABC.#.....a#\n" +
            "######...######\n" +
            "######.@.######\n" +
            "######...######\n" +
            "#b.....#.....c#\n" +
            "###############"

    @Test
    fun part2() {
        assertEquals(24, day18.part2(input7String.split("\n").map { it.toCharArray() }))
        assertEquals(8, day18.part2(input6String.split("\n").map { it.toCharArray() }))
    }
}