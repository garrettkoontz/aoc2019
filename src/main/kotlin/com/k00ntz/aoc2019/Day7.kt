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
        val input = parseLine("day7.txt") { it.split(",").map { Integer.parseInt(it) }.toIntArray() }
        measureAndPrintTime {
            print(maxThrustSignal(IntCodeComputer(input)))
        }
        measureAndPrintTime {
            print(maxLoopedThrustSignal(input))
        }
    }

    fun maxLoopedThrustSignal(input: IntArray): Int =
        loopedThrustSignal(input, allPhases(phases2).maxBy { loopedThrustSignal(input, it) }!!)


    fun loopedThrustSignal(input: IntArray, phases: List<Int>): Int {
        val ios = phases.mapIndexed { index, p -> if (index == 0) IOBuffer(listOf(p, 0)) else IOBuffer(listOf(p)) }
        val wiredComputers = (ios.indices).map {
            Thread(WiredIntCodeComputer(input, ios[it], ios[(it + 1) % ios.size]))
        }
        wiredComputers.map { it.start(); it }.forEach { it.join() }
        return ios.first().getInput()
    }

    val phases1 = (0..4).toList()
    val phases2 = (5..9).toList()

    fun allPhases(phases: List<Int>): Set<List<Int>> {
        val ap = mutableSetOf<List<Int>>()
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


    fun maxThrustSignal(intCodeComputer: IntCodeComputer): Int =
        thrusterSignal(intCodeComputer, allPhases(phases1).maxBy { thrusterSignal(intCodeComputer, it) }!!)


    fun thrusterSignal(intCodeComputer: IntCodeComputer, phases: List<Int>): Int {
        var output = 0
        for (p in phases) {
            output = intCodeComputer.executeProgram(p, output).second.last()
        }
        return output
    }

}