package com.k00ntz.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day8Test {

    val day8 = Day8()

    @Test
    fun parseLayers() {
        assertEquals(
            listOf(
                Layer(listOf(listOf(1, 2, 3), listOf(4, 5, 6))),
                Layer(listOf(listOf(7, 8, 9), listOf(0, 1, 2)))),
                day8.parseLayers(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2), 3, 2)
            )
        assertEquals(
            listOf(
                Layer(listOf(listOf(0,2), listOf(2,2))),
                Layer(listOf(listOf(1,1), listOf(2,2))),
                Layer(listOf(listOf(2,2), listOf(1,2))),
                Layer(listOf(listOf(0,0), listOf(0,0)))),
            day8.parseLayers(listOf(0,2,2,2,1,1,2,2,2,2,1,2,0,0,0,0), 2, 2)
        )
    }

    @Test
    fun layerOverLayerTest(){
        assertEquals(Layer(listOf(listOf(0,1), listOf(1,0))),
            layerOverLayer(day8.parseLayers(listOf(0,2,2,2,1,1,2,2,2,2,1,2,0,0,0,0), 2, 2)))
    }
}