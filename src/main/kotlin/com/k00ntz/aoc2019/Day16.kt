package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseLine

fun main() {
    Day16().run()
}

class Day16 : Day {

    fun phase(lst: List<Int>): List<Int> =
        lst.mapIndexed { index, _ -> partialSum(lst, index) }

    // do_comp
    fun partialSum(lst: List<Int>, index: Int): Int {
        val idx = index + 1
        return lst.foldIndexed(0) { iIndex: Int, acc: Int, i: Int ->
            val multiple = getBasePatternForIndex(idx, iIndex)
            acc + i * multiple
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
        assert(offset > input.size * 5000) { "message is not in second half!" }
        val repeatedList = repeated(input, 10000).drop(offset)
        val phased = latePhase(repeatedList, 100)
        return phased.take(8).joinToString(separator = "")
    }

    private fun latePhase(lst: List<Int>, times: Int): List<Int> =
        (0 until times).fold(lst) { acc, _ -> partialSums(acc) }


    private fun partialSums(lst: List<Int>): List<Int> =
        lst.dropLast(1).reversed().fold(mutableListOf(lst.last())) { list, e ->
            list.add((list.last() + e) % 10)
            list
        }.reversed()

    private fun repeated(lst: List<Int>, times: Int): List<Int> =
        (1..times).flatMap { lst }

    val basePattern = listOf(0, 1, 0, -1)

    fun getBasePatternForIndex(rowIndex: Int, elemIndex: Int): Int {
        val incrElemIndex = (elemIndex + 1) % (4 * rowIndex)
        return when {
            incrElemIndex < rowIndex -> 0
            incrElemIndex < 2 * rowIndex -> 1
            incrElemIndex < 3 * rowIndex -> 0
            incrElemIndex < 4 * rowIndex -> -1
            else -> throw RuntimeException("bad index! ${rowIndex / incrElemIndex}")
        }
    }
}