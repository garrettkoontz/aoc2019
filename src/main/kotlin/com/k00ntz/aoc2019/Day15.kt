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

    fun part1(input: LongArray): Int {
        val ioOutput = FixedOutput()
        val ioInput = TraceInput(ioOutput)
        val wicc = IntCodeComputer(input, output = ioOutput).executeProgram(ioInput)
        return 10
    }

    fun part2(input: Any?): Any? {
        return null
    }

    class TraceInput(
        val output: FixedOutput,
        var outIdx: Int = -1,
        var pos: Point = Point(0, 0),
        var commandHistory: Deque<Direction> = ArrayDeque(listOf(Direction.NORTH)),
        val map: MutableMap<Point, DroidResponse> = mutableMapOf(),
        var retraceFlag: Boolean = false
    ) : Input {

        enum class Direction(val inputCode: Long, val pt: Point) {
            NORTH(1L, Point(0, 1)),
            SOUTH(2L, Point(0, -1)),
            WEST(3L, Point(-1, 0)),
            EAST(4L, Point(1, 0));

            fun getOpposite() =
                when (this) {
                    NORTH -> SOUTH
                    SOUTH -> NORTH
                    WEST -> EAST
                    EAST -> WEST
                }

            fun getNext() =
                when (this) {
                    NORTH -> EAST
                    SOUTH -> WEST
                    WEST -> NORTH
                    EAST -> SOUTH
                }
        }

        enum class DroidResponse(val outputCode: Long, val str: String) : PointDrawable {
            WALL(0, "#"),
            SPACE(1, "."),
            OX_SYS(2, "S"),
            DROID(-1, "D");

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


        override fun get(): Long {
            if (outIdx == -1) return commandHistory.peekFirst().inputCode.also { outIdx++ }
            val nextCommand = when (fromOutputCode(output.values[outIdx++])) {
                DroidResponse.WALL -> {
                    map[pos + commandHistory.peekFirst().pt] = DroidResponse.WALL
                    val nextCommand = Direction.values().firstOrNull { !map.containsKey(pos + it.pt) }
                        ?: Direction.values().first { map[pos + it.pt] != DroidResponse.WALL }.also {
                            retraceFlag = true
                        }
                    commandHistory.push(nextCommand)
                    nextCommand.inputCode
                }
                DroidResponse.SPACE -> {
                    pos += commandHistory.peekFirst().pt
                    map[pos] = DroidResponse.SPACE
                    val nextCommand =
                        if (retraceFlag) {
                            Direction.values().firstOrNull { !map.containsKey(pos + it.pt) }
                                ?: Direction.values().first { map[pos + it.pt] != DroidResponse.WALL }
                        } else commandHistory.peekFirst()
                    commandHistory.push(nextCommand)
                    nextCommand.inputCode
                }
                DroidResponse.OX_SYS -> {
                    pos += commandHistory.peekFirst().pt
                    map[pos] = DroidResponse.OX_SYS
                    val nextCommand = commandHistory.peekFirst()
                    commandHistory.push(nextCommand)
                    nextCommand.inputCode
                }
                else -> {
                    throw RuntimeException("Unexpected output.")
                }
            }
            println(map.plus(Pair(pos, DroidResponse.DROID)).draw())
            println("______________________________________________")
            return nextCommand
        }


    }
}