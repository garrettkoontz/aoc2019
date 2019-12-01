package com.k00ntz.aoc2019

import com.k00ntz.aoc.utils.parseFile

fun moduleFuelCalc(mass: Int) = (mass / 3) - 2

fun moduleFuelCalcWithFuel(mass: Int): Int {
    var sum = 0
    var m = mass
    while(m > 0){
        val newM = moduleFuelCalc(m)
        sum += if(newM > 0) newM else 0
        m = newM
    }
    return sum
}

fun main() {
    val ints = parseFile("day1.txt") {Integer.parseInt(it)}
    println(ints.map { moduleFuelCalc(it) }.sum())
    println(ints.map { moduleFuelCalcWithFuel(it) }.sum())
}