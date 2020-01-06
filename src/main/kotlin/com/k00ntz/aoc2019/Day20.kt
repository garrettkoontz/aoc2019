package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.*
import java.util.*

fun main() {
    Day20().run()
}

class Day20 : Day {
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
        val recursiveMaze = parseRecursiveMaze(input)
        return recursiveDijkstra(recursiveMaze)
    }

    fun part1(input: List<CharArray>): Int {
        val maze = parseMaze(input)
        return dijkstra(maze)
    }

    data class HeapObj(
        val distance: Int,
        val depth: Int,
        val portal: Pair<String, INOUT>,
        val path: Set<Pair<String, INOUT>>
    ) : Comparable<HeapObj> {

        override fun compareTo(other: HeapObj): Int =
            this.distance.compareTo(other.distance)

    }

    private fun recursiveDijkstra(
        maze: Map<Pair<String, INOUT>, Map<Pair<String, INOUT>, Int>>,
        start: Pair<String, INOUT> = Pair("AA", INOUT.OUT),
        end: Pair<String, INOUT> = Pair("ZZ", INOUT.OUT)
    ): Int {
        val heap = PriorityQueue<HeapObj>()
        heap.offer(HeapObj(0, 0, start, setOf(start)))
        val seen = mutableSetOf<Pair<Pair<String, INOUT>, Set<Pair<String, INOUT>>>>()
        while (heap.isNotEmpty()) {
            val (distance, depth, portal, path) = heap.poll()
            if (portal == end) {
                if (depth == 0) return distance - 1
                else continue
            }
            if (portal == start && depth != 0) continue
            if (seen.contains(Pair(portal, path))) continue
            seen.add(Pair(portal, path))
            maze.getValue(portal).forEach {
                heap.offer(HeapObj(distance + it.value + 1, depth + it.key.second.value, it.key, path.plus(it.key)))
            }
        }
        return -1
    }

    enum class INOUT(val value: Int) {
        IN(1),
        OUT(-1);

        fun swap() = if (this == IN) OUT else IN
    }

    private fun Point.isOuterPoint(xSize: Int, ySize: Int): Boolean =
        this.x() == 0 || this.x() == xSize - 1 || this.y() == 0 || this.y() == ySize - 1

    private fun parseRecursiveMaze(map: List<CharArray>): Map<Pair<String, INOUT>, Map<Pair<String, INOUT>, Int>> {
        val portalMap = parseToMap(map)
        return portalMap.map { entry ->
            val pointMap = mutableMapOf<Pair<String, INOUT>, Int>()
            val firstPoint = Pair(entry.key, 0)
            val queue = ArrayDeque<Pair<Pair<Point, INOUT>, Int>>()
            val seen = mutableSetOf<Pair<Point, INOUT>>()
            queue.add(firstPoint)
            while (queue.isNotEmpty()) {
                val p = queue.pollFirst()
                if (seen.contains(p.first)) continue
                seen.add(p.first)
                val neighbors = p.first.first.validNeighbors(map) { !(it == ' ' || it == '#') }
                neighbors.forEach {
                    val swapPair = Pair(it, p.first.second.swap())
                    if (portalMap.keys.contains(swapPair))
                        pointMap[Pair(portalMap.getValue(swapPair), swapPair.second)] = p.second + 1
                    else if (!seen.contains(swapPair))
                        queue.add(Pair(Pair(it, p.first.second), p.second + 1))
                }
            }
            Pair(Pair(entry.value, entry.key.second), pointMap.toMap())
        }.toMap()
    }

    private fun parseToMap(map: List<CharArray>): MutableMap<Pair<Point, INOUT>, String> {
        val portalMap = mutableMapOf<Point, Pair<INOUT, String>>()
        for (caIdx in map.indices) {
            for (cIdx in map[caIdx].indices) {
                val c = map[caIdx][cIdx]
                if (c.isLetter()) {
                    val others = Point(cIdx, caIdx).validNeighbors(map) { it != ' ' && it != '#' }
                    val point = others.firstOrNull { map.getPoint(it) == '.' } ?: continue
                    val otherPoint = others.first { map.getPoint(it).isLetter() }
                    val otherLabel = map.getPoint(otherPoint)
                    val inOut = if (otherPoint.isOuterPoint(map[caIdx].size, map.size)) INOUT.OUT else INOUT.IN
                    portalMap[point] = Pair(inOut, if (c > otherLabel) "$otherLabel$c" else "$c$otherLabel")
                }
            }
        }
        return portalMap
    }

    private fun dijkstra(maze: Map<String, Map<String, Int>>, start: String = "AA", end: String = "ZZ"): Int {
        val heap =
            PriorityQueue<Triple<Int, String, Set<String>>>(kotlin.Comparator { o1, o2 -> o1.first.compareTo(o2.first) })
        heap.offer(Triple(0, start, setOf(start)))
        val seen = mutableSetOf<Pair<String, Set<String>>>()
        while (heap.isNotEmpty()) {
            val (d, pt, path) = heap.poll()
            if (pt == end) {
                return d - 1
            }
            if (seen.contains(Pair(pt, path))) continue
            seen.add(Pair(pt, path))
            maze.getValue(pt).forEach {
                heap.offer(Triple(d + it.value + 1, it.key, path.plus(it.key)))
            }
        }
        return -1
    }

    private fun parseMaze(map: List<CharArray>): Map<String, Map<String, Int>> {

        val portalMap = mutableMapOf<Point, String>()
        for (caIdx in map.indices) {
            for (cIdx in map[caIdx].indices) {
                val c = map[caIdx][cIdx]
                if (c.isLetter()) {
                    val others = Point(cIdx, caIdx).validNeighbors(map) { it != ' ' && it != '#' }
                    val point = others.firstOrNull { map.getPoint(it) == '.' } ?: continue
                    val otherLabel = map.getPoint(others.first { map.getPoint(it).isLetter() })
                    portalMap[point] = if (c > otherLabel) "$otherLabel$c" else "$c$otherLabel"
                }
            }
        }

        val entries = portalMap.map { entry ->
            val pointMap = mutableMapOf<String, Int>()
            val firstPoint = Pair(entry.key, 0)
            val queue = ArrayDeque<Pair<Point, Int>>()
            val seen = mutableSetOf<Point>()
            queue.add(firstPoint)
            while (queue.isNotEmpty()) {
                val p = queue.pollFirst()
                if (seen.contains(p.first)) continue
                seen.add(p.first)
                val neighbors = p.first.validNeighbors(map) { !(it == ' ' || it == '#') }
                neighbors.forEach {
                    if (portalMap.keys.contains(it) && it != entry.key)
                        pointMap[portalMap.getValue(it)] = p.second + 1
                    else if (!seen.contains(it))
                        queue.add(Pair(it, p.second + 1))
                }
            }
            Pair(entry.value, pointMap.toMap())
        }
        val outputMap = mutableMapOf<String, Map<String, Int>>()
        entries.forEach { (portal, toPortals) ->
            outputMap[portal] = outputMap.getOrDefault(portal, emptyMap()).plus(toPortals)
        }
        return outputMap
    }

}