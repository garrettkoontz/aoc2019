package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile
import java.util.*
import kotlin.math.ceil

fun main() {
    Day14().run()
}


class Day14 : Day {

    val parseEq: (String) -> Pair<String, Pair<Int, List<Pair<Int, String>>>> = { s: String ->
        val line = s.split("=>")
        val icRegex = "([0-9]+) ([A-Z]+)".toRegex()
        val inputs = icRegex.findAll(line[0]).map { mr ->
            mr.destructured.let { (num, letter) ->
                Pair(num.toInt(), letter)
            }
        }.toList()
        val output = icRegex.find(line[1])!!.destructured.let { (num, letter) -> Pair(num.toInt(), letter) }
        Pair(output.second, Pair(output.first, inputs))
    }

    override fun run() {
        val input = parseFile("${this::class.simpleName!!.toLowerCase()}.txt", parseEq).toMap()
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    private fun part2(input: Map<String, Pair<Int, List<Pair<Int, String>>>>): Long {
        return howMuchFuel(input, Pair(1, "FUEL"), "ORE", 1_000_000_000_000L)
    }

    fun howMuchFuel(
        reactions: Map<String, Pair<Int, List<Pair<Int, String>>>>,
        target: Pair<Int, String>,
        base: String,
        inputBaseAmt: Long
    ): Long {
        var fuel = 0L
        var baseQty = inputBaseAmt
        val waste = mutableMapOf<String, Int>()
        while (baseQty > 0) {
            val (baseAmt, _) = findBaseAmount(reactions, target, base, waste)
            baseQty -= baseAmt
            if (baseQty > 0) fuel++
        }
        return fuel

    }

    private fun part1(input: Map<String, Pair<Int, List<Pair<Int, String>>>>): Long =
        findBaseAmount(input, Pair(1, "FUEL"), "ORE").first


    fun findBaseAmount(
        reactions: Map<String, Pair<Int, List<Pair<Int, String>>>>,
        target: Pair<Int, String>,
        base: String,
        waste: MutableMap<String, Int> = mutableMapOf()
    ): Pair<Long, Map<String, Int>> {
        val queue = ArrayDeque<Pair<Int, String>>(listOf(target))
        var ore = 0L

        while (queue.isNotEmpty()) {
            val (qty, elem) = queue.pollFirst()
            if (elem == base) {
                ore += qty
                continue
            }
            val qWithWaste = qty - minOf(waste.getOrDefault(elem, 0), qty)
            if (waste[elem] != null) waste[elem] = waste[elem]!! - minOf(waste.getOrDefault(elem, 0), qty)
            if (qWithWaste > 0) {
                val formula = reactions.getValue(elem)
                val amtNeeded = ceil((qWithWaste.toDouble() / formula.first)).toInt()
                formula.second.forEach { queue.offer(Pair(amtNeeded * it.first, it.second)) }
                waste[elem] = waste.getOrDefault(elem, 0) + formula.first * amtNeeded - qWithWaste
            }
        }
        return Pair(ore, waste)
    }
}