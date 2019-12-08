package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.intcodecomputer.IntCodeComputer
import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile

class Day2 : Day {
    override fun run() {
        val ints = parseFile("day2.txt") { it.split(",").map { Integer.parseInt(it) }.toIntArray() }.first()
        measureAndPrintTime {
            print(ints.let {
                IntCodeComputer(ints)
                    .executeProgram(12, 2)
            }.first[0])
        }
        measureAndPrintTime {
            print(ints.let {
                val (i, _) = (0..99 * 99).map { i ->
                    Pair(i, IntCodeComputer(it, i / 100, i % 100).executeProgram().first[0])
                }.first { it.second == 19690720 }
                i
            })
        }
    }
}

fun main() {
    println("Day 2")
    Day2().run()
}