package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.*

fun main() {
    Day10().run()
}

class Day10 : Day {

    val parseLine = { rowIdx: Int, line: String ->
        line.mapIndexedNotNull { colIdx, char -> if (char == '#') Point(colIdx, rowIdx) else null }.toList()
    }

    override fun run() {
        val input =
            parseFileIndexed("day10.txt", parseLine)
        measureAndPrintTime {
            print(part1(input.flatten()).second.size)
        }
        measureAndPrintTime {
            print(part2(input.flatten())[199].let { it.x() * 100 + it.y() })
        }
    }

    fun part1(input: List<Point>): Pair<Point, Set<Point>> {
        return findSeeablePoints(input).maxBy { it.second.size }!!
    }

    fun part2(input: List<Point>): List<Point> =
        vaporizationOrder(input)

    fun vaporizationOrder(input: List<Point>): List<Point> {
        val (laserPoint, firstHits) = findSeeablePoints(input).maxBy { it.second.size }!!
        val result = firstHits.sortedBy { laserPoint.angleTo(it) }.toMutableList()
        var rest = input.minus(result)
        while (result.size < input.size) {
            val res = seenPoints(laserPoint, rest).second.sortedBy { laserPoint.angleTo(it) }
            result.addAll(res)
            rest = rest.minus(res)
        }
        return result
    }

    fun findSeeablePoints(input: List<Point>): List<Pair<Point, Set<Point>>> =
        input.map { seenPoints(it, input.minus(it)) }


    fun seenPoints(pt: Point, others: List<Point>): Pair<Point, Set<Point>> {
        val seeablePoints =
            others.groupBy { pt.angleTo(it) }.mapValues { (_, l) -> l.minBy { pt.distanceTo(it) }!! }.values.toSet()
        return Pair(pt, seeablePoints)
    }

    //2408 too high

}