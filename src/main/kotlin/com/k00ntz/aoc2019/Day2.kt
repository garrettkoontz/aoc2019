package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile

fun main() {
    val ints = parseFile("day2.txt") { it.split(",").map { Integer.parseInt(it) }.toIntArray() }.first()
    measureAndPrintTime {
        print(ints.let { IntCodeComputer(ints).executeProgram(12, 2) }.first[0])
    }
    measureAndPrintTime {
        print(ints.let {
            val intCodeComputer = IntCodeComputer(ints)
            val (noun, verb, _) = (0..99).flatMap { n ->
                (0..99).map { v ->
                    Triple(n, v, intCodeComputer.executeProgram(n, v).first[0])
                }
            }.first { it.third == 19690720 }
            noun * 100 + verb
        })
    }
}