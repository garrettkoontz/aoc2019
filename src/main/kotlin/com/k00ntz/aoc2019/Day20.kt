package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.*
import java.util.*

fun main() {
    Day20().run()
}

class Day20 : Day {
    override fun run() {
        val input = parseFile(("${this::class.simpleName!!.toLowerCase()}.txt")) { it.toCharArray() }
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    fun part2(input: List<CharArray>): Any? {
        return null
    }

    fun part1(input: List<CharArray>): Int {
        val maze = parseMaze(input)
    }

    private fun parseMaze(input: List<CharArray>): Map<String, Map<String, Int>> {

        val portalMap = mutableMapOf<String, Point>()
        for (caIdx in input.indices) {
            for (cIdx in input[caIdx].indices) {
                val c = input[caIdx][cIdx]
                if (c.isLetter()) {
                    val others = Point(cIdx, caIdx).validNeighbors(input) { it != ' ' && it != '#' }
                    val point = others.firstOrNull { input.getPoint(it) == '.' } ?: continue
                    val otherLabel = others.first { input.getPoint(it).isLetter() }
                    portalMap["$otherLabel$c"] = point
                }
            }
        }

        val outputMap = mutableMapOf<String, Map<String, Int>>()
        portalMap.map {
            val firstPoint = Triple(it.value, it.key, 0)
            val queue = ArrayDeque<Triple<Point, String, Int>>()
            queue.add(firstPoint)
            while (queue.isNotEmpty()) {
                val p = queue.pollFirst()
                val pVal = input.getPoint(p.first)
                if (pVal.isLetter()) {

                }
            }
        }

    }

}