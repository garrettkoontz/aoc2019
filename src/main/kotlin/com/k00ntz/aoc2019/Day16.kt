package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseLine
import kotlin.math.floor

fun main() {
    Day16().run()
}

class Day16 : Day {

    fun phase(lst: List<Int>): List<Int> =
        lst.mapIndexed { index, _ -> partialSum(lst, index) }

    // do_comp
    fun partialSum(lst: List<Int>, index: Int): Int {
        val idx = index + 1
        return lst.foldIndexed(0) { fIndex: Int, acc: Int, i: Int ->
            acc + i * getBasePatternForIndex(fIndex, idx)
        }.toString().last() - '0'
    }

    fun phase(i: List<Int>, times: Int): List<Int> =
        (0 until times).fold(i) { acc, _ ->
            phase(acc)
        }

    override fun run() {
        val input = parseLine(("${this::class.simpleName!!.toLowerCase()}.txt")) { it.toCharArray().map { (it - '0') } }
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    fun part1(input: List<Int>): String {
        return phase(input, 100).take(8).joinToString(separator = "")
    }

    fun part2(input: List<Int>): String {
        val offset = input.take(7).joinToString(separator = "").toInt()
        val repeatedList = repeated(input, 10000).drop(offset)
        val phased = phase(repeatedList, 100)
        return phased.drop(offset).take(8).joinToString(separator = "")
    }

    private fun repeated(lst: List<Int>, times: Int): List<Int> =
        (1..times).flatMap { lst }

    val basePattern = listOf(0, 1, 0, -1)

    fun getBasePatternForIndex(rowIndex: Int, elemIndex: Int): Int {
        val totalSize = ((rowIndex + 1) * basePattern.size)
        return basePattern[floor((((elemIndex + 1) % totalSize).toDouble() / totalSize) * basePattern.size).toInt()]
    }
}