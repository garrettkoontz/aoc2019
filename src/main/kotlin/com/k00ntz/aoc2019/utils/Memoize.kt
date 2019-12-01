package com.k00ntz.aoc.utils

class Memoize<I, O>(val func: (I) -> O): (I) -> O{
    private val cache = hashMapOf<I, O>()
    override fun invoke(p1: I): O {
        return cache.getOrPut(p1) { func(p1) }
    }
}