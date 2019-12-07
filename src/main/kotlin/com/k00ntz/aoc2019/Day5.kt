package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.intcodecomputer.IntCodeComputer
import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile

class Day5 : Day {
    override fun run() {
        val input =
            parseFile("day5.txt") { it.split(",").map { Integer.parseInt(it) }.toIntArray() }.first()
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }


    private fun part1(ints: IntArray): List<Int> =
        ints.let { IntCodeComputer(ints).executeProgram (1) }.second


    private fun part2(ints: IntArray): List<Int> =
        ints.let { IntCodeComputer(ints).executeProgram(5) }.second
}

fun main() {
    println("Day 5")
    Day5().run()
}

