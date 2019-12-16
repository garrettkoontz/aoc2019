package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile
import kotlin.math.ceil

fun main() {
    Day14().run()
}


class Day14 : Day {

    val parseEq: (String) -> Pair<Pair<Int, String>, List<Pair<Int, String>>> = { s: String ->
        val line = s.split("=>")
        val icRegex = "([0-9]+) ([A-Z]+)".toRegex()
        val inputs = icRegex.findAll(line[0]).map { mr ->
            mr.destructured.let { (num, letter) ->
                Pair(num.toInt(), letter)
            }
        }.toList()
        val output = icRegex.find(line[1])!!.destructured.let { (num, letter) -> Pair(num.toInt(), letter) }
        Pair(output, inputs)
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

    private fun part1(input: Map<Pair<Int, String>, List<Pair<Int, String>>>): Long {
        //findBaseAmount(input, mutableMapOf(), Pair(1, "FUEL"), "ORE").first
        val chemicalEQ = createChemTree(input, "ORE", "FUEL")

        return 0L
    }


    fun findBaseAmount(
        reactions: Map<String, Pair<Int, List<Pair<Int, String>>>>,
        remainder: MutableMap<String, Long>,
        target: Pair<Int, String>,
        base: String
    ): Pair<Long, MutableMap<String, Long>> {
        if (target.second == base) return Pair(target.first.toLong(), remainder)
        val (amt, formula) = reactions.getValue(target.second)
        val multiplier = ceil(target.first / amt.toDouble()).toLong()
        return formula.fold(Pair<Long, MutableMap<String, Long>>(0, remainder)) { acc, (i, t) ->
            if (acc.second.getOrDefault(t, 0) >= i) {
                acc.second[t] = acc.second[t]!! - i
                Pair(acc.first, acc.second)
            } else {
                val foundAmount = findBaseAmount(reactions, remainder, Pair(i, t), base).first * multiplier
                val newRemainder = foundAmount - target.first
                acc.second[t] = acc.second.getOrDefault(t, 0L) + newRemainder
                Pair(acc.first + findBaseAmount(reactions, remainder, Pair(i, t), base).first * multiplier, remainder)
            }
        }
    }

    fun createChemTree(
        reactions: Map<Pair<Int, String>, List<Pair<Int, String>>>,
        bottom: String,
        top: String
    ): ChemicalEQ {
        return if (bottom == top) {
            ChemicalEQ(bottom, 1, null)
        } else {
            val entry = reactions.entries.first { it.key.second == top }
            ChemicalEQ(entry.key.second, entry.key.first, entry.value.map {
                Pair(createChemTree(reactions, bottom, it.second), it.first)
            }.toMap())
        }
    }

    fun part2(input: Any?): Any? {
        return null
    }


    val base = "ORE"
    val target = "FUEL"

    fun reduceFormulas(reactions: Map<Pair<Int, String>, Map<String, Int>>, formula: Map<String, Int>):
            Map<String, Int> {
        //Map("FUEL" to 1)
        formula.map { entry ->
            val f: Map<String, Int> = reactions.entries.first { it.key.second == entry.key }.value
            if (formula.keys.intersect(f.keys).isEmpty())
                return f.mapValues { (_, v) -> v * entry.value }
            else {

            }

        }
        // Map(E to 1, A to 7)
    }

    data class ChemicalEQ(val name: String, val amt: Int, val formula: Map<ChemicalEQ, Int>?) {
        val level: Int by lazy {
            if (formula == null) 0
            else 1 + formula.keys.map { it.level }.min()!!
        }

        fun reduceToLevel(i: Int): Map<ChemicalEQ, Int> {
            if (formula == null) return emptyMap()
            if (i >= level) return formula
            formula.map { (k, v) ->
                if (k.level > i)
                else Pair(k, v)
            }.toMap()
        }
    }
}