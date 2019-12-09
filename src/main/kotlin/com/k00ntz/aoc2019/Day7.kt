package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.intcodecomputer.IOBuffer
import com.k00ntz.aoc2019.intcodecomputer.IntCodeComputer
import com.k00ntz.aoc2019.intcodecomputer.WiredIntCodeComputer
import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseLine

fun main() {
    Day7().run()
}

class Day7 : Day {
    override fun run() {
        val input = parseLine("day7.txt") { it.split(",").map { it.toLong() }.toLongArray() }
        measureAndPrintTime {
            print(maxThrustSignal(IntCodeComputer(input)))
        }
        measureAndPrintTime {
            print(maxLoopedThrustSignal(input))
        }
    }

    fun maxLoopedThrustSignal(input: LongArray): Long =
        loopedThrustSignal(input, allPhases(phases2).maxBy { loopedThrustSignal(input, it) }!!)


    fun loopedThrustSignal(input: LongArray, phases: List<Long>): Long {
        val ios = phases.mapIndexed { index, p -> if (index == 0) IOBuffer(listOf(p, 0L)) else IOBuffer(listOf(p)) }
        val wiredComputers = (ios.indices).map {
            Thread(WiredIntCodeComputer(input, ios[it], ios[(it + 1) % ios.size]))
        }
        wiredComputers.map { it.start(); it }.forEach { it.join() }
        return ios.first().getInput()
    }

    val phases1 = (0L..4L).toList()
    val phases2 = (5L..9L).toList()

    fun allPhases(phases: List<Long>): Set<List<Long>> {
        val ap = mutableSetOf<List<Long>>()
        for (i in phases) {
            for (j in phases.minus(i)) {
                for (k in phases.minus(listOf(i, j))) {
                    for (l in phases.minus(listOf(i, j, k))) {
                        for (m in phases.minus(listOf(i, j, k, l)))
                            ap.add(listOf(i, j, k, l, m))
                    }
                }
            }
        }
        return ap
    }


    fun maxThrustSignal(intCodeComputer: IntCodeComputer): Long =
        thrusterSignal(intCodeComputer, allPhases(phases1).maxBy { thrusterSignal(intCodeComputer, it) }!!)


    fun thrusterSignal(intCodeComputer: IntCodeComputer, phases: List<Long>): Long {
        var output = 0L
        for (p in phases) {
            output = intCodeComputer.executeProgram(p, output).second.last()
        }
        return output
    }

}