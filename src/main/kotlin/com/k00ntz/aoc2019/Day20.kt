package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.*
import java.util.*

fun main() {
    Day20().run()
}

typealias Portal = Pair<String, Day20.INOUT>

fun Portal.swapInOut() =
    Pair(this.first, this.second.swap())

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
        return bfs(recursiveMaze)
    }

    fun part1(input: List<CharArray>): Int {
        val maze = parseMaze(input)
        return dijkstra(maze)
    }

    data class HeapObj(
        val distance: Int,
        val depth: Int,
        val portal: Pair<String, INOUT>,
        val path: Set<Triple<String, INOUT, Int>>
    ) : Comparable<HeapObj> {

        override fun compareTo(other: HeapObj): Int =
            this.distance.compareTo(other.distance).let {
                if (it == 0) this.depth.compareTo(other.depth) else it
            }

    }

    private fun bfs(
        maze: Map<Portal, Map<Portal, Pair<Int, Int>>>,
        start: String = "AA",
        end: String = "ZZ"
    ): Int {
        val maxDepth = maze.size
        val startNode = maze.keys.first { it.first == start }
        val endNode = maze.keys.first { it.first == end }
        val queue = ArrayDeque<Triple<Portal, Int, Int>>()
        queue.add(Triple(startNode, 0, 0))
        while (queue.isNotEmpty()) {
            val p: Triple<Portal, Int, Int> = queue.pollFirst()
            if (p.first.first == endNode.first)
                if (p.second < 0)
                    return p.third
                else continue
            if (p.second < 0 || p.second > maxDepth)
                continue
            maze.getValue(p.first).forEach { (portal, distanceDepthPair) ->
                queue.add(
                    Triple(
                        portal,
                        distanceDepthPair.second + p.second,
                        distanceDepthPair.first + 1 + p.third
                    )
                )
            }
        }
        return -1
    }

    private fun recursiveDijkstra(
        maze: Map<Pair<String, INOUT>, Map<Pair<String, INOUT>, Pair<Int, Int>>>,
        start: String = "AA",
        end: String = "ZZ"
//        , maxDepth: Int = 100000
    ): Int {
        val heap = PriorityQueue<HeapObj>()
        heap.offer(HeapObj(0, 0, Pair(start, INOUT.OUT), setOf(Triple(start, INOUT.OUT, 0))))
        val seen = mutableSetOf<Triple<String, INOUT, Int>>()
        while (heap.isNotEmpty()) {
            val (distance, depth, portal, path) = heap.poll()
            if (portal.first == end) {
                if (depth < 0)
                    return distance - 1
                else continue
            }
            if (depth < 0) continue
            if (portal.first == start && distance != 0) continue
            val seenTriple = Triple(portal.first, portal.second, depth)
            if (seen.contains(seenTriple)) continue
            seen.add(seenTriple)
            maze.getValue(portal).forEach {
                //                if(depth + it.key.second.value <= maxDepth)
                heap.offer(
                    HeapObj(
                        distance + it.value.first + 1,
                        depth + it.value.second,
                        Pair(it.key.first, it.key.second),
                        path.plus(Triple(it.key.first, it.key.second, depth + it.value.second))
                    )
                )
            }
        }
        return -1
    }

    enum class INOUT(val value: Int) {
        IN(1),
        OUT(-1);

        fun swap() = if (this == IN) OUT else IN
    }

    fun getInOut(p: Point, xSize: Int, ySize: Int): INOUT =
        if (p.isOuterPoint(xSize, ySize)) INOUT.OUT else INOUT.IN

    private fun Point.isOuterPoint(xSize: Int, ySize: Int): Boolean =
        this.x() == 0 || this.x() == xSize - 1 || this.y() == 0 || this.y() == ySize - 1

    private fun parseRecursiveMaze(map: List<CharArray>): Map<Pair<String, INOUT>, Map<Pair<String, INOUT>, Pair<Int, Int>>> {
        val portalMap: Map<Point, Pair<INOUT, String>> = parseToMap(map)
//        val returnMap =
        return portalMap.map { entry ->
            val pointMap = mutableMapOf<Pair<String, INOUT>, Pair<Int, Int>>()
            val firstPoint = Triple(entry.key, entry.value.first, 0)
            val queue = ArrayDeque<Triple<Point, INOUT, Int>>()
            val seen = mutableSetOf<Pair<Point, INOUT>>()
            queue.add(firstPoint)
            while (queue.isNotEmpty()) {
                val p = queue.pollFirst()
                if (seen.contains(Pair(p.first, p.second))) continue
                seen.add(Pair(p.first, p.second))
                val neighbors = p.first.validNeighbors(map) { !(it == ' ' || it == '#') }
                neighbors.forEach {
                    if (portalMap.keys.contains(it))
                        pointMap[portalMap.getValue(it).swap()] =
                            Pair(p.third + 1, portalMap.getValue(it).swap().second.value)
                    else if (!seen.contains(Pair(it, p.second)))
                        queue.add(Triple(it, p.second, p.third + 1))
                }
            }
            Pair(portalMap.getValue(entry.key).swap(), pointMap)
        }.toMap().toMutableMap()
//        return consolidate(returnMap)
    }

    private fun consolidate(
        map: MutableMap<Pair<String, INOUT>, MutableMap<Pair<String, INOUT>, Pair<Int, Int>>>
    )
            : Map<Pair<String, INOUT>, Map<Pair<String, INOUT>, Pair<Int, Int>>> {
        map.forEach { entry ->
            entry.value.remove(entry.key)
        }
        val specialstrings = setOf("AA", "ZZ")
        val multilinks = map.filter { it.value.size > 1 }
            .map { (key, mapValue) ->
                Pair(key, mapValue.map {
                    var distance = it.value.first
                    var depth = it.value.second
                    var node = Pair(it.key.first, it.key.second.swap())
                    while (map.containsKey(node) && map[node]!!.size == 1) {
                        val nextNode = map[node]!!.entries.first()
                        val n = nextNode.value
                        distance += n.first + 1
                        depth += n.second
                        node = Pair(nextNode.key.first, nextNode.key.second.swap())
                        if (specialstrings.contains(nextNode.key.first))
                            break
                    }
                    Pair(node, Pair(distance, depth))
                }.toMap())
            }.toMap()

        return multilinks
    }

    private fun parseToMap(map: List<CharArray>): MutableMap<Point, Pair<INOUT, String>> {
        val portalMap = mutableMapOf<Point, Pair<INOUT, String>>()
        for (caIdx in map.indices) {
            for (cIdx in map[caIdx].indices) {
                val c = map[caIdx][cIdx]
                if (c.isLetter()) {
                    val others = Point(cIdx, caIdx).validNeighbors(map) { it != ' ' && it != '#' }
                    val point = others.firstOrNull { map.getPoint(it) == '.' } ?: continue
                    val otherPoint = others.first { map.getPoint(it).isLetter() }
                    val otherLabel = map.getPoint(otherPoint)
                    val inOut = getInOut(otherPoint, map[caIdx].size, map.size)
                    portalMap[point] = Pair(inOut, if (point > otherPoint) "$otherLabel$c" else "$c$otherLabel")
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

private fun <A, B> Pair<A, B>.swap(): Pair<B, A> =
    Pair(this.second, this.first)
