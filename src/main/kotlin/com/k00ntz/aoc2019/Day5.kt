package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile

fun main() {
    val input =
        parseFile("day5.txt") { it.split(",").map { Integer.parseInt(it) }.toIntArray() }.first()
    measureAndPrintTime {
        print(part1(input))
    }
    measureAndPrintTime {
        print(part2(input))
    }
}

fun part1(ints: IntArray): List<Int> =
    ints.let { IntCodeComputer(ints).executeProgram(null, null, 1) }.second


fun part2(ints: IntArray): List<Int> =
    ints.let { IntCodeComputer(ints).executeProgram(null, null, 5) }.second

