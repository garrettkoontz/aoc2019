package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.*

enum class Direction(val xMove: Int, val yMove: Int) {
    U(0, 1), D(0, -1), L(-1, 0), R(1, 0)
}

data class ManhattanDirection(val dir: Direction, val count: Int) {
    constructor(s: String) : this(Direction.valueOf(s.take(1)), Integer.parseInt(s.drop(1)))

    fun pointsBetween(startPoint: Point): List<Point> {
        val movePoint = Point(this.dir.xMove, this.dir.yMove)
        return (1..count).map { startPoint.plus(movePoint.times(it)) }
    }
}

class Day3 : Day {
    override fun run() {
        val paths = parseFile("day3.txt") { l -> l.split(",").map { ManhattanDirection(it.trim()) } }
        measureAndPrintTime {
            print(findClosestIntersection(paths[0], paths[1]))
        }
        measureAndPrintTime {
            print(findFirstIntersection(paths[0], paths[1]))
        }
    }

    private fun findClosestIntersection(path1: List<ManhattanDirection>, path2: List<ManhattanDirection>): Int {
        val points1 = traceWire(path1).drop(1)
        val points2 = traceWire(path2).drop(1)
        val center = Point(0, 0)
        return points1.intersect(points2).minBy { pt -> pt.manhattanDistanceto(center) }!!.manhattanDistanceto(center)
    }

    private fun findFirstIntersection(path1: List<ManhattanDirection>, path2: List<ManhattanDirection>): Int {
        val points1 = traceWire(path1).drop(1).withIndex().groupBy({ iv -> iv.value }, { iv -> iv.index + 1 })
        val points2 = traceWire(path2).drop(1).withIndex().groupBy({ iv -> iv.value }, { iv -> iv.index + 1 })
        return points1.flatMap {
            if (points2.keys.contains(it.key))
                listOf(Pair(it.value.min(), points2.getValue(it.key).min()))
            else {
                emptyList()
            }
        }.minBy { it.first!! + it.second!! }.let { it!!.first!! + it.second!! }
    }

    private fun traceWire(paths: List<ManhattanDirection>): List<Point> {
        val startPoint = Point(0, 0)
        return paths.fold(listOf(startPoint)) { s, md ->
            s.plus(md.pointsBetween(s.last()))
        }
    }
}

fun main() {
    println("Day 3")
    Day3().run()
}