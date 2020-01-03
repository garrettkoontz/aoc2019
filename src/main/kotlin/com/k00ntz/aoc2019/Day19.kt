package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.intcodecomputer.FixedInput
import com.k00ntz.aoc2019.intcodecomputer.FixedOutput
import com.k00ntz.aoc2019.intcodecomputer.IntCodeComputer
import com.k00ntz.aoc2019.intcodecomputer.parseIntComputerInput
import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.PointDrawable
import com.k00ntz.aoc2019.utils.measureAndPrintTime

fun main() {
    Day19().run()
}

class Day19 : Day {
    override fun run() {
        val input = parseIntComputerInput(("${this::class.simpleName!!.toLowerCase()}.txt"))
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    private fun part2(input: LongArray): Long {
        val icc = IntCodeComputer(input)
        var line = 4L
        var previousXStart = 0L
        while (line < 100000) {
            (0..10).first {
                previousXStart += 1
                icc.executeProgram(FixedInput(listOf(previousXStart, line)), FixedOutput()).second.values.last() == 1L
            }
            var internalPointer = 0
            while (icc.executeProgram(
                    FixedInput(listOf(previousXStart + internalPointer, line)),
                    FixedOutput()
                ).second.values.last() == 1L
            ) {
                if (icc.executeProgram(
                        FixedInput(listOf(previousXStart + internalPointer + 100, line)),
                        FixedOutput()
                    ).second.values.last() == 1L
                    && icc.executeProgram(
                        FixedInput(listOf(previousXStart + internalPointer, line + 100)),
                        FixedOutput()
                    ).second.values.last() == 1L
                ) return 10000 * (previousXStart + internalPointer) + line
                else internalPointer++
            }
            line++
        }
        return -1
    }

    private fun part1(input: LongArray): Long {
        val icc = IntCodeComputer(input)
        (0 until 50 * 50).forEach { i ->
            icc.executeProgram(i % 50L, i / 50L)
        }
        return icc.output.values.sum()
    }
}

fun Long.toDrawable(): PointDrawable {
    return if (this == 1L) object : PointDrawable {
        override val default: String
            get() = "#"

        override fun draw(): String {
            return "#"
        }
    } else object : PointDrawable {
        override val default: String
            get() = "."

        override fun draw(): String {
            return "."
        }
    }
}