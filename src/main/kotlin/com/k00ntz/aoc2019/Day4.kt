package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile
import kotlin.streams.toList

fun main() {
    val input =
        parseFile("day4.txt") { l -> l.split("-") }.first()
            .let { Pair(Integer.parseInt(it[0]), Integer.parseInt(it[1])) }
    measureAndPrintTime {
        print(countPasswords(input.first, input.second))
    }
    measureAndPrintTime {
        print(countPasswords2(input.first, input.second))
    }
}

fun countPasswords2(min: Int, max: Int): Int =
    (min..max).filter { hasAscInts(it) && hasADouble(it) }.size

fun countPasswords(min: Int, max: Int): Int =
    (min..max).filter { hasAscInts(it) && atleastTwoDigitsAreTheSame(it) }.size

fun hasADouble(i: Int): Boolean =
    i.toString().chars().toList().groupBy { it }.entries.firstOrNull { it.value.size == 2 } != null


fun atleastTwoDigitsAreTheSame(i: Int): Boolean {
    val charsList = i.toString().chars().toList()
    return charsList.size > charsList.toSet().size
}

fun hasAscInts(i: Int): Boolean =
    i.toString().chars().toList() == i.toString().chars().sorted().toList()
