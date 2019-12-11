package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile

fun main() {
    Day12().run()
}

class Day12 : Day {
    override fun run() {
        val input = parseFile("${this::class.simpleName!!.toLowerCase()}.txt") {}
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    fun part1(input: Any?): Any? {
        return null
    }

    fun part2(input: Any?): Any? {
        return null
    }
}