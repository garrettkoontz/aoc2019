package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.*

fun main() {
    Day10().run()
}

class Day10 : Day {
    override fun run() {
        val input =
            parseFile("day10.txt") { }.first()
        measureAndPrintTime {
            print(part1(input))
        }
//        measureAndPrintTime {
//            print(part2(input))
//        }
    }

    fun part1(input: List<Point>): Int {
        input.map { seenPoints(it, input.minus(it)) }.maxBy { it.second.size }!!.first
        return 0
    }


    fun seenPoints(pt: Point, others: List<Point>): Pair<Point, Set<Point>> =
        Pair(
            pt,
            others.map {
                colinear(
                    pt,
                    it,
                    others.minus(it)
                ).plus(it)
            }.toSet().map { lst -> lst.minBy { pt.distanceTo(it) }!! }.toSet()
        )


    fun colinear(pt1: Point, pt2: Point, others: List<Point>): List<Point> =
        others.filter { colinear(pt1, pt2, it) }


    fun colinear(pt1: Point, pt2: Point, pt3: Point): Boolean =
        pt1.y() - pt2.y() * (pt1.x() - pt3.x()) == (pt1.y() - pt3.y()) * (pt1.x() - pt2.x())


}