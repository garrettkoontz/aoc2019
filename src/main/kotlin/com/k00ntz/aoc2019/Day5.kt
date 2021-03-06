package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.intcodecomputer.IntCodeComputer
import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile

class Day5 : Day {
    override fun run() {
        val input =
            parseFile("day5.txt") { it.split(",").map { it.toLong() }.toLongArray() }.first()
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }


    private fun part1(ints: LongArray): List<Long> =
        ints.let { IntCodeComputer(ints).executeProgram(1L) }.second


    private fun part2(ints: LongArray): List<Long> =
        ints.let { IntCodeComputer(ints).executeProgram(5L) }.second
}

fun main() {
    println("Day 5")
    Day5().run()
}

