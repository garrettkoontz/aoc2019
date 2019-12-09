package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseLine
import kotlin.streams.toList

data class Layer(val ints: List<List<Int>>) {
    override fun toString(): String =
        ints.joinToString(separator = "\n") { l -> l.joinToString(separator = "") { if (it == 0) " " else it.toString() } }
}

fun layerOverLayer(layers: List<Layer>): Layer {
    val returnInts = layers.first().ints.map { it.toMutableList() }
    val lowers = layers.drop(1)
    for (i in returnInts.indices) {
        for (j in 0 until returnInts[i].size) {
            if (returnInts[i][j] == 2) {
                val lowerPixel: Layer? = lowers.firstOrNull { it.ints[i][j] != 2 }
                if (lowerPixel != null)
                    returnInts[i][j] = lowerPixel.ints[i][j]
            }
        }
    }
    return Layer(returnInts)
}

fun main() {
    Day8().run()
}

class Day8 : Day {
    val width = 25
    val height = 6

    override fun run() {
        val input = parseLine("day8.txt") { it.chars().map { c -> c - '0'.toInt() }.toList() }
        val layers = parseLayers(input, width, height)
        measureAndPrintTime {
            print(checkSum(layers))
        }
        measureAndPrintTime {
            print(layerOverLayer(layers))
        }
    }

    fun checkSum(layers: List<Layer>): Int {
        val minLayer =
            layers.minBy { lyr ->
                lyr.ints.flatten().filter {
                    it == 0
                }.size
            }!!
                .ints.flatten().groupBy { it }
        return minLayer.getValue(1).size * minLayer.getValue(2).size
    }

    fun parseLayers(ints: List<Int>, width: Int, height: Int): List<Layer> =
        ints.chunked(width * height).map { layerInts ->
            Layer((0 until height).map { layerInts.subList(it * width, (it + 1) * width) })
        }


}