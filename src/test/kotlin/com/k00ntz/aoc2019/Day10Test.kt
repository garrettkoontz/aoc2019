package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.Point
import com.k00ntz.aoc2019.utils.angleTo
import com.k00ntz.aoc2019.utils.filterColinear
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day10Test {

    val day10 = Day10()

    val input1 = ".#..#\n" +
            ".....\n" +
            "#####\n" +
            "....#\n" +
            "...##"
    val list1 = input1.split("\n").mapIndexed(day10.parseLine).flatten()
    val input2 = "......#.#.\n" +
            "#..#.#....\n" +
            "..#######.\n" +
            ".#.#.###..\n" +
            ".#..#.....\n" +
            "..#....#.#\n" +
            "#..#....#.\n" +
            ".##.#..###\n" +
            "##...#..#.\n" +
            ".#....####"
    val list2 = input2.split("\n").mapIndexed(day10.parseLine).flatten()
    val input3 = "#.#...#.#.\n" +
            ".###....#.\n" +
            ".#....#...\n" +
            "##.#.#.#.#\n" +
            "....#.#.#.\n" +
            ".##..###.#\n" +
            "..#...##..\n" +
            "..##....##\n" +
            "......#...\n" +
            ".####.###."
    val list3 = input3.split("\n").mapIndexed(day10.parseLine).flatten()
    val input4 = ".#..#..###\n" +
            "####.###.#\n" +
            "....###.#.\n" +
            "..###.##.#\n" +
            "##.##.#.#.\n" +
            "....###..#\n" +
            "..#.#..#.#\n" +
            "#..#.#.###\n" +
            ".##...##.#\n" +
            ".....#.#.."
    val list4 = input4.split("\n").mapIndexed(day10.parseLine).flatten()
    val input5 = ".#..##.###...#######\n" +
            "##.############..##.\n" +
            ".#.######.########.#\n" +
            ".###.#######.####.#.\n" +
            "#####.##.#.##.###.##\n" +
            "..#####..#.#########\n" +
            "####################\n" +
            "#.####....###.#.#.##\n" +
            "##.#################\n" +
            "#####.##.###..####..\n" +
            "..######..##.#######\n" +
            "####.##.####...##..#\n" +
            ".#####..#.######.###\n" +
            "##...#.##########...\n" +
            "#.##########.#######\n" +
            ".####.#.###.###.#.##\n" +
            "....##.##.###..#####\n" +
            ".#.#.###########.###\n" +
            "#.#.#.#####.####.###\n" +
            "###.##.####.##.#..##"
    val list5 = input5.split("\n").mapIndexed(day10.parseLine).flatten()

    @Test
    fun part1() {
        val (pt1, seenPoints1) = day10.part1(list1)
        assertEquals(Point(3, 4), pt1)
        assertEquals(8, seenPoints1.size)
        val (pt3, seenPoints3) = day10.part1(list3)
        assertEquals(Point(1, 2), pt3)
        assertEquals(35, seenPoints3.size)
        val (pt5, seenPoints5) = day10.part1(list5)
        assertEquals(210, seenPoints5.size)
        assertEquals(Point(11, 13), pt5)
        val (pt2, seenPoints2) = day10.part1(list2)
        assertEquals(33, seenPoints2.size)
        assertEquals(Point(5, 8), pt2)
        val (pt4, seenPoints4) = day10.part1(list4)
        assertEquals(41, seenPoints4.size)
        assertEquals(Point(6, 3), pt4)
    }

    @Test
    fun part2() {
        assertEquals(0.0, Point(11, 13).angleTo(Point(11, 12)), 0.000001)
        val vaporOrder = day10.part2(list5)
        assertEquals(Point(8, 2), vaporOrder[199])
    }

    @Test
    fun findSeeablePoints() {
        assertEquals(
            list1.minus(listOf(Point(3, 4), Point(1, 0))).toSet(),
            day10.findSeeablePoints(list1).first { it.first == Point(3, 4) }.second
        )
        assertEquals(33, day10.findSeeablePoints(list2).first { it.first == Point(5, 8) }.second.size)
    }

    @Test
    fun seenPoints() {
        assertEquals(
            list1.minus(listOf(Point(3, 4), Point(1, 0))).toSet(),
            day10.seenPoints(Point(3, 4), list1.minus(Point(3, 4))).second
        )
        assertEquals(
            setOf(Point(1, 1), Point(-1, 1), Point(1, 0)), day10.seenPoints(
                Point(0, 0), listOf(
                    Point(1, 0), Point(-1, 1), Point(-2, 2), Point(-3, 3),
                    Point(1, 1), Point(2, 2), Point(3, 3)
                )
            ).second
        )
    }

    @Test
    fun colinear() {
        assertEquals(
            listOf(Point(0, 0), Point(1, 1), Point(2, 2), Point(3, 3)), Point(0, 0).filterColinear(
                Point(3, 3), listOf(
                    Point(1, 0), Point(-1, 1), Point(-2, 2), Point(-3, 3),
                    Point(0, 0), Point(1, 1), Point(2, 2), Point(3, 3)
                )
            )
        )
    }

    @Test
    fun parse() {
        assertEquals(listOf(Point(0, 0)), day10.parseLine(0, "#"))
        assertEquals(listOf(Point(0, 0), Point(2, 0)), day10.parseLine(0, "#.#"))
    }
}