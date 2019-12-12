package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.*

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

    fun step(posVolPair: List<Pair<Point3, Point3>>): List<Pair<Point3, Point3>> {
        val gravity = posVolPair.map { p ->
            posVolPair.minus(p).fold(Point3(0, 0, 0)) { acc, p2 ->
                acc + gravity(p.first, p2.first)
            }
        }
        return posVolPair.zip(gravity).map { (pv, g) -> Pair(pv.first + g + pv.second, pv.second + g) }
    }

    fun gravity(p1: Point3, p2: Point3): Point3 =
        Point3(p1.x().compareTo(p2.x()), p1.y().compareTo(p2.y()), p1.z().compareTo(p2.z()))

}