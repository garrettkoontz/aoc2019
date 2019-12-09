package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.intcodecomputer.IntCodeComputer
import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile


fun main() {
    Day9().run()
}

class Day9 : Day {
    override fun run() {
        val input =
            parseFile("day9.txt") { it.split(",").map { it.toLong() }.toLongArray() }.first()
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    fun part1(input: LongArray): List<Long> =
        IntCodeComputer(input).executeProgram(1L).second

    fun part2(input: LongArray): List<Long> =
        IntCodeComputer(input).executeProgram(2L).second


}