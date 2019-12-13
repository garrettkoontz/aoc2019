package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.intcodecomputer.*
import com.k00ntz.aoc2019.utils.*

fun main() {
    Day13().run()
}

class Day13 : Day {
    override fun run() {
        val input = parseIntComputerInput("${this::class.simpleName!!.toLowerCase()}.txt")
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    fun part1(input: LongArray): Int {
        val output = FixedOutput()
        IntCodeComputer(input, output = output).executeProgram()
        return output.values.chunked(3).filter { if (it.size == 3) it[2] == 2L else false }.size
    }

    fun part2(input: LongArray): Long {
        val ioInput = IOBuffer()
        val ioOutput = IOBuffer()
        val wicc = WiredIntCodeComputer(input, ioInput, ioOutput, mapOf(0 to 2L))
        val p = Thread(Player(ioInput, ioOutput))
        val t = Thread(wicc)
        p.start()
        t.start()
        p.join()
        return -1L
    }

    class Player(val inputBuffer: IOBuffer, val outputBuffer: IOBuffer) : Runnable {
        enum class TileType(val i: Int, val str: String) {
            EMPTY(0, " "),
            WALL(1, "W"),
            BLOCK(2, "B"),
            PADDLE(3, "P"),
            BALL(4, "O");

            override fun toString(): String {
                return str
            }
        }

        val tileMap = TileType.values().associateBy { it.i }

        fun parseOutput(o: Triple<Int, Int, Int>): Pair<Point, TileType> =
            Pair(Point(o.first, o.second), tileMap.getValue(o.third))


        override fun run() {
            inputBuffer.buffer.put(0)
            val (triples, other) = outputBuffer.buffer.toList().chunked(3).partition { it.size == 3 }
            val map: Map<Point, TileType> =
                triples.associate { parseOutput(Triple(it[0].toInt(), it[1].toInt(), it[2].toInt())) }
            println(map.draw())
            val string = readLine()!!
            println(string)
        }

        fun Map<Point, TileType>.toLists(): List<MutableList<TileType>> {
            val maxWidth = this.keys.maxBy { it.first }!!.first
            val minWidth = this.keys.minBy { it.first }!!.first
            val maxHeight = this.keys.maxBy { it.second }!!.second
            val minHeight = this.keys.minBy { it.second }!!.second
            val wideArray = (minWidth..maxWidth).map { TileType.EMPTY }.toList()
            val printGrid = (minHeight..maxHeight).map { wideArray.toMutableList() }
            this.entries.forEach { (pt, t) ->
                printGrid[pt.y() - minHeight][pt.x() - minWidth] = t
            }
            return printGrid
        }

        fun Map<Point, TileType>.draw(): String {
            return this.toLists()
                .joinToString(separator = "\n") { l -> l.joinToString(separator = "") { it.toString() } }
        }
    }
}

