package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.*
import java.util.*

fun main() {
    Day18().run()
}

interface Item {
    val point: Point
}

data class Key(override val point: Point, val c: Char) : Item
data class Bot(override val point: Point) : Item

class Day18 : Day {
    override fun run() {
        val input = parseFile(("${this::class.simpleName!!.toLowerCase()}.txt")) { it.toCharArray() }
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    fun part2(input: List<CharArray>): Int {
        val newInput = input.toMutableList()
        outer@ for (rows in newInput.indices) {
            for (cols in newInput[rows].indices) {
                if (newInput[rows][cols] == '@') {
                    newInput[rows - 1][cols - 1] = '@'
                    newInput[rows + 1][cols - 1] = '@'
                    newInput[rows + 1][cols + 1] = '@'
                    newInput[rows - 1][cols + 1] = '@'
                    newInput[rows][cols] = '#'
                    newInput[rows - 1][cols] = '#'
                    newInput[rows + 1][cols] = '#'
                    newInput[rows][cols - 1] = '#'
                    newInput[rows][cols + 1] = '#'
                    break@outer
                }
            }
        }
        return dijkstra(conditionalKeys(newInput))
    }

    fun part1(map: List<CharArray>): Int =
        dijkstra(conditionalKeys(map))

    private fun dijkstra(map: Map<Item, Map<Key, Pair<Int, Set<Char>>>>): Int {
        val allKeys = map.keys.filterIsInstance<Key>()
        val heap =
            PriorityQueue<Triple<Int, Item, Set<Key>>>(kotlin.Comparator { o1, o2 -> o1.first.compareTo(o2.first) })
        val bots = map.keys.filter { it is Bot }
        bots.forEach { heap.offer(Triple(0, it, emptySet())) }
        val seen = mutableSetOf<Pair<Point, Set<Key>>>()
        while (heap.isNotEmpty()) {
            val (d, k, currentKeys) = heap.poll()
            if (currentKeys.containsAll(allKeys)) {
                return d
            }
            if (seen.contains(Pair(k.point, currentKeys))) continue
            seen.add(Pair(k.point, currentKeys))
            val nodes = map.getValue(k)
            nodes.filter {
                if (currentKeys.isEmpty()) it.value.second.isEmpty() else currentKeys.map { k -> k.c.toUpperCase() }.containsAll(
                    it.value.second
                )
            }.forEach { (t, u) ->
                heap.offer(Triple(d + u.first, t, currentKeys.plus(t)))
            }
        }
        return -1
    }

    private fun allKeys(map: List<CharArray>) =
        map.mapIndexed { rowIdx, chars ->
            chars.mapIndexed { colIdx, c ->
                when {
                    keyRegex.matches(c.toString()) -> Key(Point(colIdx, rowIdx), c)
                    botRegex.matches(c.toString()) -> Bot(Point(colIdx, rowIdx))
                    else -> null
                }
            }
        }.flatten().filterNotNull().toSet()


    private fun conditionalKeys(map: List<CharArray>): Map<Item, Map<Key, Pair<Int, Set<Char>>>> =
        allKeys(map).associateBy(keySelector = { it }, valueTransform = { item ->
            val result = mutableMapOf<Key, Pair<Int, Set<Char>>>()
            val queue = ArrayDeque<Triple<Point, Int, Set<Char>>>(listOf(Triple(item.point, 0, emptySet())))
            val seen = mutableSetOf(item.point)
            while (queue.isNotEmpty()) {
                val (pt, length, doors) = queue.pollFirst()
                val pVal = map.getPointOrNull(pt) ?: continue
                if (keyRegex.matches(pVal.toString()) && !seen.contains(pt) && !result.containsKey(Key(pt, pVal))) {
                    result[Key(pt, pVal)] = Pair(length, doors)
                }
                validNeighbors(pt, map).minus(seen).forEach {
                    val nPt = map.getPoint(it)
                    if (doorRegex.matches(nPt.toString()))
                        queue.addLast(Triple(it, length + 1, doors.plus(nPt)))
                    else
                        queue.addLast(Triple(it, length + 1, doors))
                }
                seen.add(pt)
            }
            result
        })

    private val keyRegex = "[a-z]".toRegex()
    private val doorRegex = "[A-Z]".toRegex()
    private val botRegex = "@".toRegex()
    private val matchingRegex = "[^#]".toRegex()

    private fun validNeighbors(pos: Point, map: List<CharArray>): Set<Point> =
        setOf(pos + Point(0, 1), pos + Point(0, -1), pos + Point(1, 0), pos + Point(-1, 0))
            .filter {
                it.x() >= 0 && it.x() < map.first().size
                        && it.y() >= 0 && it.y() < map.size
                        && matchingRegex.matches(map.getPoint(it).toString())
            }
            .toSet()

}