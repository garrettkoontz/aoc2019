package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.intcodecomputer.IntCodeComputer
import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile

class Day2 : Day {
    override fun run() {
        val ints = parseFile("day2.txt") { it.split(",").map { it.toLong() }.toLongArray() }.first()
        measureAndPrintTime {
            print(ints.let {
                IntCodeComputer(ints)
                    .executeProgram(12, 2)
            }.first[0])
        }
        measureAndPrintTime {
            print(ints.let {
                val (i, _) = (0..99 * 99).map { i ->
                    Pair(i, IntCodeComputer(it, i / 100L, i % 100L).executeProgram().first[0])
                }.first { it.second == 19690720L }
                i
            })
        }
    }
}

fun main() {
    println("Day 2")
    Day2().run()
}