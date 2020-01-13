package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile
import java.util.*
import kotlin.streams.toList

fun main() {
    Day24().run()
}

val blankRow = listOf(0, 0, 0, 0, 0)

class BugMap(val map: SortedMap<Int, List<List<Int>>>) {

    constructor(c: List<List<Int>>) : this(mapOf(0 to c).toSortedMap())

    fun newList(eight: Int, twelve: Int, fourteen: Int, eighteen: Int): List<List<Int>> =
        listOf(
            listOf(
                (eight + twelve in 1..2).toInt(),
                (eight > 0).toInt(),
                (eight > 0).toInt(),
                (eight > 0).toInt(),
                (eight + fourteen in 1..2).toInt()
            ),
            listOf((twelve > 0).toInt(), 0, 0, 0, (fourteen > 0).toInt()),
            listOf((twelve > 0).toInt(), 0, 2, 0, (fourteen > 0).toInt()),
            listOf((twelve > 0).toInt(), 0, 0, 0, (fourteen > 0).toInt()),
            listOf(
                (eighteen + twelve in 1..2).toInt(),
                (eighteen > 0).toInt(),
                (eighteen > 0).toInt(),
                (eighteen > 0).toInt(),
                (eighteen + fourteen in 1..2).toInt()
            )
        )

    fun nextMap(): BugMap {
        val nextMap = mutableMapOf<Int, List<List<Int>>>()
        val maxKey = map.lastKey()
        val minKey = map.firstKey()
        map[maxKey]!!.let {
            val eight = it[1][2]
            val twelve = it[2][1]
            val fourteen = it[2][3]
            val eighteen = it[3][2]
            if (eight in 1..2 || twelve in 1..2 || fourteen in 1..2 || eighteen in 1..2) {
                nextMap[maxKey + 1] = newList(eight, twelve, fourteen, eighteen)
            }
        }
        map[minKey]!!.let {
            val eight = it.first().sum()
            val twelve = it.map { it.first() }.sum()
            val fourteen = it.map { it.last() }.sum()
            val eighteen = it.last().sum()
            if (eight in 1..2 || twelve in 1..2 || fourteen in 1..2 || eighteen in 1..2) {
                nextMap[minKey - 1] = newList(eight, twelve, fourteen, eighteen)
            }
        }
        var key = maxKey
        while (key > minKey - 1) {
            val subLevel = map[key - 1] ?: listOf(listOf(0))
            val top = subLevel.first().sum()
            val left = subLevel.map { it.first() }.sum()
            val right = subLevel.map { it.last() }.sum()
            val bottom = subLevel.last().sum()
            val upperLevel = map[key + 1] ?: (0..4).map { blankRow }
            val eight = upperLevel[1][2]
            val twelve = upperLevel[2][1]
            val fourteen = upperLevel[2][3]
            val eighteen = upperLevel[3][2]
            fun List<List<Int>>.neighbors(yIndex: Int, xIndex: Int): Int {
                return when {
                    yIndex == 1 && xIndex == 2 -> {
                        this[yIndex - 1][xIndex] + top + this[yIndex][xIndex - 1] + this[yIndex][xIndex + 1]
                    }
                    yIndex == 2 && xIndex == 1 -> {
                        this[yIndex - 1][xIndex] + this[yIndex + 1][xIndex] + this[yIndex][xIndex - 1] + left
                    }
                    yIndex == 2 && xIndex == 3 -> {
                        this[yIndex - 1][xIndex] + this[yIndex + 1][xIndex] + right + this[yIndex][xIndex + 1]
                    }
                    yIndex == 3 && xIndex == 2 -> {
                        bottom + this[yIndex + 1][xIndex] + this[yIndex][xIndex - 1] + this[yIndex][xIndex + 1]
                    }
                    else -> (if (yIndex > 0) this[yIndex - 1][xIndex] else eight) +
                            (if (yIndex < 4) this[yIndex + 1][xIndex] else eighteen) +
                            (if (xIndex > 0) this[yIndex][xIndex - 1] else twelve) +
                            (if (xIndex < 4) this[yIndex][xIndex + 1] else fourteen)
                }
            }

            val lst = map[key]!!
            nextMap[key] = lst.mapIndexed { yIndex, list ->
                list.mapIndexed { xIndex, i ->
                    if (!(yIndex == 2 && xIndex == 2)) {
                        val neighborsCount = lst.neighbors(yIndex, xIndex)
                        if ((lst[yIndex][xIndex] == 1 && neighborsCount == 1) ||
                            (lst[yIndex][xIndex] == 0 && (neighborsCount == 1 || neighborsCount == 2))
                        ) 1
                        else 0
                    } else {
                        2
                    }
                }
            }
            key--
        }

        return BugMap(nextMap.toSortedMap())
    }

    fun count(): Int =
        map.map { it.value.map { it.filter { it < 2 }.sum() }.sum() }.sum()

}

private fun Boolean.toInt(): Int =
    if (this) 1 else 0

class Day24 : Day {
    override fun run() {
        val input = parseFile(("${this::class.simpleName!!.toLowerCase()}.txt")) {
            it.chars().mapToObj { it == '#'.toInt() }.map { if (it) 1 else 0 }.toList()
        }
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    private fun part2(input: List<List<Int>>): Int {
        val outputMap = (0 until 200).fold(BugMap(input)) { acc: BugMap, _: Int -> acc.nextMap() }
        return outputMap.count()
    }


    fun part1(input: List<List<Int>>): Any? {
        val seen = mutableSetOf<List<List<Int>>>()
        var i = input
        while (!seen.contains(i)) {
            seen.add(i)
            i = next(i)
        }
        return biodiversity(i)
    }

    private fun biodiversity(i: List<List<Int>>): Int =
        Integer.parseUnsignedInt(i.joinToString(separator = "") {
            it.joinToString(separator = "")
        }.reversed(), 2)

    fun next(lst: List<List<Int>>): List<List<Int>> =
        lst.mapIndexed { yIndex, list ->
            list.mapIndexed { xIndex, i ->
                val neighborsCount = lst.neighbors(yIndex, xIndex)
                if ((lst[yIndex][xIndex] == 1 && neighborsCount == 1) ||
                    (lst[yIndex][xIndex] == 0 && (neighborsCount == 1 || neighborsCount == 2))
                ) 1
                else 0
            }
        }

    fun List<List<Int>>.neighbors(yIndex: Int, xIndex: Int): Int =
        (if (yIndex > 0) this[yIndex - 1][xIndex] else 0) +
                (if (yIndex < 4) this[yIndex + 1][xIndex] else 0) +
                (if (xIndex > 0) this[yIndex][xIndex - 1] else 0) +
                (if (xIndex < 4) this[yIndex][xIndex + 1] else 0)

}

fun List<List<Int>>.asString(): String =
    this.joinToString(separator = "\n") {
        it.joinToString(separator = "")
    }

