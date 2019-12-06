package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile

class Day1 : Day {
    override fun run() {
        val ints = parseFile("day1.txt") { Integer.parseInt(it) }
        measureAndPrintTime { print(ints.map { moduleFuelCalc(it) }.sum()) }
        measureAndPrintTime { print(ints.map { moduleFuelCalcWithFuel(it) }.sum()) }
    }

    internal fun moduleFuelCalc(mass: Int) = (mass / 3) - 2

    internal fun moduleFuelCalcWithFuel(mass: Int): Int {
        var sum = 0
        var m = mass
        while (m > 0) {
            val newM = moduleFuelCalc(m)
            sum += if (newM > 0) newM else 0
            m = newM
        }
        return sum
    }
}

fun main() {
    println("Day 1")
    Day1().run()
}