package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.intcodecomputer.FixedInput
import com.k00ntz.aoc2019.intcodecomputer.FixedOutput
import com.k00ntz.aoc2019.intcodecomputer.IntCodeComputer
import com.k00ntz.aoc2019.intcodecomputer.parseIntComputerInput
import com.k00ntz.aoc2019.utils.*

fun main() {
    Day17().run()
}

class Day17 : Day {
    override fun run() {
        val input = parseIntComputerInput("${this::class.simpleName!!.toLowerCase()}.txt")
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    private fun part2(input: LongArray): Any? {
        val ioOutput = FixedOutput()
        val ioInput = FixedInput(emptyList())
        IntCodeComputer(input, output = ioOutput).executeProgram(ioInput)
        val mapString = String(ioOutput.values.map { it.toChar() }.toCharArray())
        val map: List<CharArray> = mapString
            .split("\n").map { it.toCharArray() }.dropLast(2)
        val path = tracePath(map)
//        val (a, b, c) = findGroups(path) // would be nice to do this programmatically.
        val inputList = ("A,B,A,B,C,B,C,A,C,C\n" +
                "R,12,L,10,L,10\n" +
                "L,6,L,12,R,12,L,4\n" +
                "L,12,R,12,L,6\n" +
                "n\n").toCharArray().map { it.toLong() }

        val ioOutput2 = FixedOutput()
        val ioInput2 = FixedInput(inputList)
        IntCodeComputer(input.also { it[0] = 2 }, output = ioOutput2).executeProgram(ioInput2)
        return ioOutput2.values.dropLast(1).last()
    }

    private fun part1(input: LongArray): Int {
        val ioOutput = FixedOutput()
        val ioInput = FixedInput(emptyList())
        IntCodeComputer(input, output = ioOutput).executeProgram(ioInput)
        val mapString = String(ioOutput.values.map { it.toChar() }.toCharArray())
//        println(mapString)
        val map: List<CharArray> = mapString
            .split("\n").map { it.toCharArray() }
        val intersects = findIntersections(map)
        return intersects.map { it.first * it.second }.sum()
    }

    fun findIntersections(map: List<CharArray>): List<Point> =
        map.drop(1).dropLast(1).mapIndexed { rowIdx, ca ->
            ca.mapIndexed { colIdx, c ->
                if (colIdx > 0 && colIdx < ca.size - 1 &&
                    c == '#' && ca[colIdx - 1] == '#' && ca[colIdx + 1] == '#'
                    && map[rowIdx][colIdx] == '#' && map[rowIdx + 2][colIdx] == '#'
                )
                    Pair(colIdx, rowIdx + 1)
                else
                    null
            }
        }.flatten().filterNotNull()

    enum class Direction(val char: Char, val movePoint: Point) {
        UP('^', Point(0, -1)),
        RIGHT('>', Point(1, 0)),
        DOWN('v', Point(0, 1)),
        LEFT('<', Point(-1, 0)),
        GONE('X', Point(0, 0));


        fun getTurn(pt: Point): Pair<String, Direction> {
            return when (this) {
                UP -> when (pt) {
                    Point(1, 0) -> Pair("R", RIGHT)
                    Point(-1, 0) -> Pair("L", LEFT)
                    else -> throw RuntimeException("invalid turn")
                }
                DOWN -> when (pt) {
                    Point(1, 0) -> Pair("L", RIGHT)
                    Point(-1, 0) -> Pair("R", LEFT)
                    else -> throw RuntimeException("invalid turn")
                }
                RIGHT -> when (pt) {
                    Point(0, 1) -> Pair("R", DOWN)
                    Point(0, -1) -> Pair("L", UP)
                    else -> throw RuntimeException("invalid turn")
                }
                LEFT -> when (pt) {
                    Point(0, 1) -> Pair("L", DOWN)
                    Point(0, -1) -> Pair("R", UP)
                    else -> throw RuntimeException("invalid turn")
                }
                else -> throw RuntimeException("invalid turn")
            }

        }
    }

    fun isDirection(c: Char): Boolean =
        setOf('^', '>', '<', 'v').contains(c)


    fun getDirection(c: Char): Direction =
        when (c) {
            '^' -> Direction.UP
            '>' -> Direction.RIGHT
            '<' -> Direction.LEFT
            'v' -> Direction.DOWN
            'X' -> Direction.GONE
            else -> throw RuntimeException("not a direction: $c")
        }


    private fun tracePath(map: List<CharArray>): String {
        val startRowIdx = map.indexOfFirst { it.any { isDirection(it) } }
        val startColIdx = map[startRowIdx].indexOfFirst { isDirection(it) }
        var pos = Point(startColIdx, startRowIdx)
        var direction = getDirection(map.getPoint(pos))
        val visited = mutableListOf(pos)
        val expectedVisits =
            map.flatMap { it.toList() }.filter { it == '#' }.size + findIntersections(map).size + 1 // for the starting point
        val moves = mutableListOf<String>()
        fun getNeighbors(pos: Point): Set<Point> =
            setOf(pos + Point(0, 1), pos + Point(0, -1), pos + Point(1, 0), pos + Point(-1, 0))
                .filter {
                    it.x() >= 0 && it.x() < map.first().size
                            && it.y() >= 0 && it.y() < map.size
                            && map.getPoint(it) == '#'
                }
                .toSet()
        while (visited.size < expectedVisits) {
            val neighbors = getNeighbors(pos)
            //if we want to keep going
            if (neighbors.contains(pos + direction.movePoint)) {
                if (moves.lastOrNull()?.toIntOrNull() != null) {
                    moves[moves.size - 1] = (moves.last().toInt() + 1).toString()
                } else {
                    moves.add("1")
                }
            } else {
                // we have to turn
                val (move, newDir) = direction.getTurn(neighbors.minus(visited).first() - pos)
                moves.add(move)
                moves.add("1")
                direction = newDir
            }
            pos += direction.movePoint
            visited.add(pos)
        }
        return moves.joinToString(prefix = "", separator = ",", postfix = "\n")
    }


}
