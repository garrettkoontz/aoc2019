package com.k00ntz.aoc2019.utils

class Ring<T : Any>(private val list: List<T>, private val size: Int = list.size, private var start: Int = 0) :
    Iterable<T> {
    override fun iterator(): Iterator<T> = object : Iterator<T> {
        override fun hasNext(): Boolean = true

        override fun next(): T = list[start % size].also { start++ }

    }
}