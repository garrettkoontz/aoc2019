package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.*

fun main() {
    Day12().run()
}

class Day12 : Day {
    override fun run() {
        val input = parseFile("${this::class.simpleName!!.toLowerCase()}.txt") {
            Point3.fromString(it) to Point3(0, 0, 0)
        }
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input.map { it.first }))
        }
    }

    fun part2(input: List<Point3>): Long =
        cycles(input).reduce { acc, l -> lcm(acc, l) }

    data class QuadHelper(val first: Int, val second: Int, val third: Int, val fourth: Int) {

        fun gravityQuad(): QuadHelper {
            val fs = first.compareTo(second)
            val ft = first.compareTo(third)
            val ff = first.compareTo(fourth)
            val st = second.compareTo(third)
            val sf = second.compareTo(fourth)
            val tf = third.compareTo(fourth)
            return QuadHelper(-fs - ft - ff, -st - sf + fs, -tf + st + ft, tf + sf + ff)
        }

        operator fun plus(other: QuadHelper): QuadHelper =
            QuadHelper(
                this.first + other.first,
                this.second + other.second,
                this.third + other.third,
                this.fourth + other.fourth
            )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is QuadHelper) return false

            if (first != other.first) return false
            if (second != other.second) return false
            if (third != other.third) return false
            if (fourth != other.fourth) return false

            return true
        }

        override fun hashCode(): Int {
            var result = first
            result = 31 * result + second
            result = 31 * result + third
            result = 31 * result + fourth
            return result
        }
    }

    fun List<Int>.toQuadHelper(): QuadHelper =
        QuadHelper(this[0], this[1], this[2], this[3])

    fun cycles(lst: List<Point3>): List<Long> =
        listOf(
            lst.map { it.x }.toQuadHelper(),
            lst.map { it.y }.toQuadHelper(),
            lst.map { it.z }.toQuadHelper()
        ).pmap { getCycleLength(it) }


    fun getCycleLength(position: QuadHelper, velocity: QuadHelper = QuadHelper(0, 0, 0, 0)): Long {
        var vel = velocity
        var pos = position
        var i = 0L
        do {
            i++
            val gravity = pos.gravityQuad()
            vel += gravity
            pos += vel
        } while (vel != velocity || pos != position)
        return i
    }

    fun part1(input: List<Pair<Point3, Point3>>): Int =
        totalEnergy(takeSteps(input, 1000))


    fun totalEnergy(lst: List<Pair<Point3, Point3>>): Int =
        lst.map { it.first.energy() * it.second.energy() }.sum()

    fun takeSteps(lst: List<Pair<Point3, Point3>>, i: Int): List<Pair<Point3, Point3>> =
        (0 until i).fold(lst) { acc, _ ->
            step(acc)
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
        Point3(p2.x.compareTo(p1.x), p2.y.compareTo(p1.y), p2.z.compareTo(p1.z))

}

