package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.intcodecomputer.IOBuffer
import com.k00ntz.aoc2019.intcodecomputer.WiredIntCodeComputer
import com.k00ntz.aoc2019.intcodecomputer.parseIntComputerInput
import com.k00ntz.aoc2019.utils.*

fun main() {
    Day11().run()
}

class Day11 : Day {
    override fun run() {
        val input =
            parseIntComputerInput("day11.txt")
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    private fun part1(input: LongArray): Int =
        Painter(input).paint().size

    private fun part2(input: LongArray): String =
        Painter(input, paintedGrid = mutableMapOf(Point(0, 0) to 1)).paint().draw()

    fun Map<Point, Int>.draw(): String {
        val maxWidth = this.keys.maxBy { it.first }!!.first
        val minWidth = this.keys.minBy { it.first }!!.first
        val maxHeight = this.keys.maxBy { it.second }!!.second
        val minHeight = this.keys.minBy { it.second }!!.second
        val wideArray = (minWidth..maxWidth).map { 0 }.toList()
        val printGrid = (minHeight..maxHeight).map { wideArray.toMutableList() }
        this.entries.forEach { (pt, i) ->
            printGrid[pt.y() - minHeight][pt.x() - minWidth] = i
        }
        return printGrid.joinToString(separator = "\n") { l -> l.joinToString(separator = "") { if (it == 0) " " else it.toString() } }
    }

}

class Painter(
    private val inputInstructions: LongArray,
    private val startingPoint: Point = Point(0, 0),
    private val paintedGrid: MutableMap<Point, Int> = mutableMapOf()
) {
    fun paint(): Map<Point, Int> {
        val inputIO = IOBuffer()
        val outputIO = IOBuffer()
        val codeThread =
            Thread(WiredIntCodeComputer(inputInstructions, inputIO, outputIO), "intCodeComputer")
        val ioHandler = Thread(IOHandler(outputIO, inputIO, paintedGrid, startingPoint), "ioHandler")
        ioHandler.start()
        codeThread.start()
        ioHandler.join()
        codeThread.join()
        return paintedGrid
    }

    private class IOHandler(
        val output: IOBuffer,
        val input: IOBuffer,
        val paintedGrid: MutableMap<Point, Int>,
        val startingPoint: Point
    ) : Runnable {
        override fun run() {
            var currentPoint = startingPoint
            var currentDirection = Direction.U
            input.send(paintedGrid.getOrDefault(currentPoint, 0).toLong())
            while (true) {
                val nextColor = output.get()
                if (nextColor == -1L) break
                val nextDirection = output.get()
                paintedGrid[currentPoint] = nextColor.toInt()
                currentDirection = currentDirection.getNextDirection(nextDirection.toInt())
                currentPoint = currentPoint.plus(currentDirection.toPoint())
                input.send(paintedGrid.getOrDefault(currentPoint, 0).toLong())
            }
        }
    }

}

fun Direction.getNextDirection(i: Int): Direction {
    val enumValues = enumValues<Direction>()
    return when (i) {
        0 -> enumValues[(enumValues.indexOf(this) + 1) % 4]
        1 -> enumValues[(enumValues<Direction>().indexOf(this) + 3) % 4]
        else -> throw RuntimeException("invalid next direction value")
    }
}