package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile
import java.math.BigInteger

fun main() {
    Day22().run()
}

sealed class ShuffleIns {
    abstract fun operateOn(ia: List<Long>): List<Long>
    abstract fun inverse(size: Long, l: Long): Long
}

object NewStack : ShuffleIns() {
    override fun operateOn(ia: List<Long>): List<Long> =
        ia.reversed()

    override fun inverse(size: Long, l: Long): Long =
        size - l - 1
}

class Cut(val n: Int) : ShuffleIns() {
    override fun operateOn(ia: List<Long>): List<Long> =
        if (n >= 0) {
            ia.subList(n, ia.size).plus(ia.subList(0, n))
        } else {
            ia.subList(ia.size + n, ia.size).plus(ia.subList(0, ia.size + n))
        }

    override fun inverse(size: Long, l: Long): Long =
        (l + n + size) % size

}

class Deal(val increment: Long) : ShuffleIns() {
    override fun operateOn(ia: List<Long>): List<Long> {
        val arrList = ia.toMutableList()
        ia.forEachIndexed { index, i -> arrList[((index * increment) % ia.size).toInt()] = i }
        return arrList.toList()
    }

    override fun inverse(size: Long, l: Long): Long =
        BigInteger.valueOf(increment).modInverse(BigInteger.valueOf(size))
            .multiply(BigInteger.valueOf(l))
            .mod(BigInteger.valueOf(size))
            .toLong()

}

class Day22 : Day {
    private val spaceCardsSize = 10007L

    override fun run() {
        val input = parseFile(("${this::class.simpleName!!.toLowerCase()}.txt")) { parseShuffleInstruction(it) }
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    private fun parseShuffleInstruction(it: String): ShuffleIns {
        val newStackRegex = "deal into new stack".toRegex()
        val cutRegex = "cut (.*)".toRegex()
        val dealIncrementRegex = "deal with increment (.*)".toRegex()
        return when {
            newStackRegex.matches(it) -> NewStack
            cutRegex.matches(it) -> cutRegex.matchEntire(it)!!.destructured.let { (n) -> Cut(n.toInt()) }
            dealIncrementRegex.matches(it) -> dealIncrementRegex.matchEntire(it)!!.destructured.let { (n) -> Deal(n.toInt()) }
            else -> throw RuntimeException("can't parse '$it'")
        }

    }

    fun part2(input: List<ShuffleIns>): Any? {
        val deckSize = 119_315_717_514_047
        val mappedDeck = shuffleDeck(input, (0 until deckSize).toList())
        return mappedDeck.take(2020)
    }

    fun part1(input: List<ShuffleIns>, deckSize: Long = spaceCardsSize): Int {
        return shuffleDeck(input, (0 until deckSize).toList()).indexOf(2019)
    }

    fun shuffleDeck(shuffles: List<ShuffleIns>, deck: List<Long>): List<Long> =
        shuffles.fold(deck) { acc: List<Long>, shuffleIns: ShuffleIns ->
            shuffleIns.operateOn(acc)
        }
}