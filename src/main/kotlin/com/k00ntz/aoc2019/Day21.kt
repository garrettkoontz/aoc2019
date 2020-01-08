package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.intcodecomputer.IntCodeComputer
import com.k00ntz.aoc2019.intcodecomputer.parseIntComputerInput
import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime


fun main() {
    Day21().run()
}

class Day21 : Day {
    override fun run() {
        val input = parseIntComputerInput(("${this::class.simpleName!!.toLowerCase()}.txt"))
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }


    private fun part1(input: LongArray): Long {
        val icc = IntCodeComputer(input)
        val (_, output) = icc.executeProgram(
            *(
                    "NOT A T\n" +
                            "NOT B J\n" +
                            "OR T J\n" +
                            "NOT C T\n" +
                            "OR T J\n" +
                            "AND D J\n" +
                            "WALK\n").chars().mapToLong { it.toLong() }.toArray()
        )
        println(output.map { it.toChar() }.joinToString(separator = ""))
        return output.last()
    }

    private fun part2(input: LongArray): Any? {
        val icc = IntCodeComputer(input)
        val (_, output) = icc.executeProgram(
            *(
                    "NOT A T\n" +
                            "OR T J \n" +
                            "NOT B T\n" +
                            "AND D T\n" +
                            "OR T J \n" +
                            "NOT C T\n" +
                            "AND D T\n" +
                            "AND H T\n" +
                            "OR T J\n" +
                            "RUN\n").chars().mapToLong { it.toLong() }.toArray()
        )
        println(output.map { it.toChar() }.joinToString(separator = ""))
        return output.last()
    }

}