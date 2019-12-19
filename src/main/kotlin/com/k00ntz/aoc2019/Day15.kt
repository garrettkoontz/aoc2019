package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.intcodecomputer.FixedOutput
import com.k00ntz.aoc2019.intcodecomputer.Input
import com.k00ntz.aoc2019.intcodecomputer.IntCodeComputer
import com.k00ntz.aoc2019.intcodecomputer.parseIntComputerInput
import com.k00ntz.aoc2019.utils.*
import java.util.*

fun main() {
    Day15().run()
}

class Day15 : Day {
    override fun run() {
        val input = parseIntComputerInput("${this::class.simpleName!!.toLowerCase()}.txt")
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    var m: Map<Point, TraceInput.DroidResponse> = emptyMap()

    fun part1(input: LongArray): Int {
        val ioOutput = FixedOutput()
        val ioInput = TraceInput(ioOutput)
        try {
            IntCodeComputer(input, output = ioOutput).executeProgram(ioInput)
        } catch (p: PoisonPill) {
//            println(p.map.draw())
            m = p.map
            return tracePath(p.map)
        }
        return 0
    }

    fun tracePath(m: Map<Point, TraceInput.DroidResponse>): Int {
        var points: Set<Point> = setOf(Point(0, 0))
        val target: Point = m.entries.first { it.value == TraceInput.DroidResponse.OX_SYS }.key
        var steps = 0
        while (!points.contains(target)) {
            steps++
            points = points.flatMap { p ->
                TraceInput.Direction.values().map { d -> p + d.pt }.filter { m[it] != TraceInput.DroidResponse.WALL }
            }.toSet()
        }
        return steps
    }

    fun fillOxygen(m: Map<Point, TraceInput.DroidResponse>, oxygenPoint: Point): Int {
        val points: MutableSet<Point> = mutableSetOf(oxygenPoint)
        var nextPoints: Set<Point> = setOf(oxygenPoint)
        var steps = -1
        while (nextPoints.isNotEmpty()) {
            steps++
            nextPoints = nextPoints.flatMap { p ->
                TraceInput.Direction.values().map { d -> p + d.pt }.filter { m[it] != TraceInput.DroidResponse.WALL }
            }.toSet().minus(points)
            points.addAll(nextPoints)
        }
        return steps
    }

    fun part2(input: Any?): Any? {
        val target: Point = m.entries.first { it.value == TraceInput.DroidResponse.OX_SYS }.key
        return fillOxygen(m, target)
    }

    class PoisonPill(val map: Map<Point, TraceInput.DroidResponse>) : Throwable()

    class TraceInput(
        val output: FixedOutput,
        var outIdx: Int = -1,
        var pos: Point = Point(0, 0),
        var commandHistory: Deque<Direction> = ArrayDeque(listOf(Direction.NORTH)),
        val map: MutableMap<Point, DroidResponse> = mutableMapOf(Pair(pos, DroidResponse.ORIGIN))
    ) : Input {

        fun values(seed: Int) =
            Direction.values().also { it.sortBy { (it.inputCode + (seed * 31) % 4) % 4 } }

        enum class Direction(val inputCode: Long, val pt: Point) {
            NORTH(1L, Point(0, 1)),
            SOUTH(2L, Point(0, -1)),
            WEST(3L, Point(-1, 0)),
            EAST(4L, Point(1, 0));

            fun reverse() =
                when (this) {
                    NORTH -> SOUTH
                    EAST -> WEST
                    SOUTH -> NORTH
                    WEST -> EAST
                }
        }

        enum class DroidResponse(val outputCode: Long, val str: String) : PointDrawable {
            WALL(0, "#"),
            SPACE(1, "."),
            OX_SYS(2, "S"),
            DROID(-1, "D"),
            ORIGIN(-2, "O");

            override val default: String = " "
            override fun draw(): String = this.str
        }

        fun fromOutputCode(outputCode: Long): DroidResponse =
            when (outputCode) {
                0L -> DroidResponse.WALL
                1L -> DroidResponse.SPACE
                2L -> DroidResponse.OX_SYS
                else -> throw RuntimeException("Unknown output code $outputCode")
            }

        val retraceDeque = ArrayDeque<Direction>()

        fun nextCommand(same: Boolean = true): Long =
            if (retraceDeque.size > 0 || !same || pos.distanceTo(Point(0, 0)) > 50) {
                Direction.values().firstOrNull { !map.containsKey(pos + it.pt) }?.also {
                    retraceDeque.clear()
                    commandHistory.push(it)
                }
                    ?: if (commandHistory.isNotEmpty()) commandHistory.pollFirst().reverse().also { retraceDeque.push(it) }
                    else throw PoisonPill(map)
            } else {
                commandHistory.peekFirst().also { commandHistory.push(it) }
            }.inputCode


        override fun get(): Long {
            if (outIdx == -1) return commandHistory.peekFirst().inputCode.also { outIdx++ }
            val nextCommand = when (fromOutputCode(output.values[outIdx++])) {
                DroidResponse.WALL -> {
                    val move =
                        if (retraceDeque.isEmpty()) commandHistory.peekFirst().pt else retraceDeque.peekFirst().pt
                    map[pos + move] = DroidResponse.WALL
                    commandHistory.pollFirst()
                    nextCommand(false)
                }
                DroidResponse.SPACE -> {
                    pos += if (retraceDeque.isEmpty()) commandHistory.peekFirst().pt else retraceDeque.peekFirst().pt
                    map[pos] = DroidResponse.SPACE
                    nextCommand()
                }
                DroidResponse.OX_SYS -> {
                    pos += if (retraceDeque.isEmpty()) commandHistory.peekFirst().pt else retraceDeque.peekFirst().pt
                    map[pos] = DroidResponse.OX_SYS
                    nextCommand()
                }
                else -> {
                    throw RuntimeException("Unexpected output.")
                }
            }
//            println(map.plus(Pair(pos, DroidResponse.DROID)).draw())
//            println("______________________________________________")
            return nextCommand
        }


    }
}