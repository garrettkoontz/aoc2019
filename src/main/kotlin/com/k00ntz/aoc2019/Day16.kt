package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.pMapIndexed
import com.k00ntz.aoc2019.utils.parseLine
import kotlin.math.abs

fun main() {
    Day16().run()
}

class Day16 : Day {

    val basePattern = listOf(0, 1, 0, -1)

    fun getBasePatternForIndex(rowIndex: Int, elemIndex: Int): Int {
        val totalSize = ((rowIndex + 1) * basePattern.size)
        val idx = (elemIndex + 1) / totalSize
        return basePattern[idx]
    }


    fun phase(i: List<Int>): List<Int> =
        i.pMapIndexed { iIndex, _ ->
            abs(i.mapIndexed { jIndex, j ->
                (j * getBasePatternForIndex(iIndex, jIndex)) % 10
            }.sum() % 10)
        }

    fun phase(i: List<Int>, times: Int): List<Int> =
        (0 until times).fold(i) { acc, _ ->
            phase(acc)
        }

    override fun run() {
        val input = parseLine(("${this::class.simpleName!!.toLowerCase()}.txt")) { it.toCharArray().map { '0' - it } }
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    private fun part2(input: List<Int>): String {
        return phase(input, 100).take(8).joinToString(separator = "")
    }

    private fun part1(input: Any?): Any? {
        return null
    }
}