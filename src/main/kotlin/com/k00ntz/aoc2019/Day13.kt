package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.intcodecomputer.FixedOutput
import com.k00ntz.aoc2019.intcodecomputer.Input
import com.k00ntz.aoc2019.intcodecomputer.IntCodeComputer
import com.k00ntz.aoc2019.intcodecomputer.parseIntComputerInput
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
        val ioOutput = FixedOutput()
        val ioInput = HackInput(ioOutput)
        val wicc = IntCodeComputer(input.also { it[0] = 2 }, output = ioOutput).executeProgram(ioInput)
        return wicc.second.chunked(3).last { it.size == 3 && it[0] == -1L }[2]
    }

    class HackInput(val output: FixedOutput) : Input {
        var consumeIdx = 0
        val map = mutableMapOf<Point, Value>()

        enum class TileType(val i: Int, val str: String) {
            EMPTY(0, " "),
            WALL(1, "W"),
            BLOCK(2, "B"),
            PADDLE(3, "-"),
            BALL(4, "O"),
            NEXT_PADDLE(-1, "p");

            override fun toString(): String {
                return str
            }

        }


        data class Value(val tileType: TileType?, val score: Long?)

        val tileMap = TileType.values().associateBy { it.i }

        fun parseOutput(o: Triple<Int, Int, Int>): Pair<Point, Value> =
            Pair(
                Point(o.first, o.second),
                if (o.first == -1) Value(null, o.third.toLong()) else Value(tileMap.getValue(o.third), null)
            )

        override fun get(): Long {
            val (trips, _) = output.values.subList(consumeIdx, output.values.size).chunked(3).partition { it.size == 3 }
            map.putAll(trips.associate { parseOutput(Triple(it[0].toInt(), it[1].toInt(), it[2].toInt())) })
//            drawOutputBuffer(map)
            val paddle = map.filter { it.value.tileType == TileType.PADDLE }.keys.first()
            val ball = map.filter { it.value.tileType == TileType.BALL }.keys.first()
            val input = if (paddle.x() < ball.x()) 1L else if (paddle.x() > ball.x()) -1L else 0L
            consumeIdx = output.values.size
            return input
        }

        fun drawOutputBuffer(map: Map<Point, Value>) {
            println(map.draw())
            println("score: ${map[Point(-1, 0)]}")
        }

        fun Map<Point, Value>.toLists(): List<MutableList<String>> {
            val maxWidth = this.keys.maxBy { it.first }!!.first
            val minWidth = 0
            val maxHeight = this.keys.maxBy { it.second }!!.second
            val minHeight = this.keys.minBy { it.second }!!.second
            val wideArray = (minWidth..maxWidth).map { TileType.EMPTY.toString() }.toList()
            val printGrid = (minHeight..maxHeight).map { wideArray.toMutableList() }
            this.entries.filter { it.key != Point(-1, 0) }.forEach { (pt, t) ->
                printGrid[pt.y() - minHeight][pt.x() - minWidth] = t.tileType!!.toString()
            }
            return printGrid
        }

        fun Map<Point, Value>.draw(): String {
            return this.toLists()
                .joinToString(separator = "\n") { l -> l.joinToString(separator = "") { it } }
        }

    }

    /*class Player(val inputBuffer: IOBuffer, val outputBuffer: IOBuffer) : Runnable {

        enum class TileType(val i: Int, val str: String) {
            EMPTY(0, " "),
            WALL(1, "W"),
            BLOCK(2, "B"),
            PADDLE(3, "-"),
            BALL(4, "O"),
            NEXT_PADDLE(-1, "p");

            override fun toString(): String {
                return str
            }
        }

        data class Value(val tileType: TileType?, val score: Long?)

        val tileMap = TileType.values().associateBy { it.i }

        fun parseOutput(o: Triple<Int, Int, Int>): Pair<Point, Value> =
            Pair(
                Point(o.first, o.second),
                if (o.first == -1) Value(null, o.third.toLong()) else Value(tileMap.getValue(o.third), null)
            )

        override fun run() {
            val map = mutableMapOf<Point, Value>()
            while (true) {
                if (inputBuffer.awaiting.get() > 0) {
                    val lst = mutableListOf<Long>()
                    outputBuffer.buffer.drainTo(lst)
                    val (trips, _) = lst.chunked(3).partition { it.size == 3 }
                    map.putAll(trips.associate { parseOutput(Triple(it[0].toInt(), it[1].toInt(), it[2].toInt())) })
                    drawOutputBuffer(map)
                    val blocks = map.filter { it.value.tileType == TileType.BLOCK }.size
                    if (blocks > 0) {
                        val paddle = map.filter { it.value.tileType == TileType.PADDLE }.keys.first()
                        val ball = map.filter { it.value.tileType == TileType.BALL }.keys.first()
                        val input = if(paddle.x() < ball.x()) 1L else if(paddle.x() > ball.x()) -1L else 0L
                        println("Paddle: $paddle")
                        println("Ball: $ball")
                        println(input)
                        inputBuffer.send(input)
                    } else {
                        break
                    }
                }
            }
        }

        fun drawOutputBuffer(map: Map<Point, Value>) {
            println(map.draw())
            println("score: ${map[Point(-1, 0)]}")
        }

        fun Map<Point, Value>.toLists(): List<MutableList<String>> {
            val maxWidth = this.keys.maxBy { it.first }!!.first
            val minWidth = 0
            val maxHeight = this.keys.maxBy { it.second }!!.second
            val minHeight = this.keys.minBy { it.second }!!.second
            val wideArray = (minWidth..maxWidth).map { TileType.EMPTY.toString() }.toList()
            val printGrid = (minHeight..maxHeight).map { wideArray.toMutableList() }
            this.entries.filter { it.key != Point(-1, 0) }.forEach { (pt, t) ->
                printGrid[pt.y() - minHeight][pt.x() - minWidth] = t.tileType!!.toString()
            }
            return printGrid
        }

        fun Map<Point, Value>.draw(): String {
            return this.toLists()
                .joinToString(separator = "\n") { l -> l.joinToString(separator = "") { it } }
        }
    }*/
}

