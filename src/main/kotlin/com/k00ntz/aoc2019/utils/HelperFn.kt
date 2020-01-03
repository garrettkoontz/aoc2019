package com.k00ntz.aoc2019.utils

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.stream.Collectors
import kotlin.streams.toList
import kotlin.system.measureTimeMillis

fun measureAndPrintTime(block: () -> Unit) {
    val time = measureTimeMillis(block)
    println(" took $time ms")
}

fun List<CharArray>.getPoint(pt: Point): Char =
    this[pt.y()][pt.x()]

fun List<CharArray>.getPointOrNull(pt: Point): Char? {
    if (pt.y() < 0 || pt.y() >= this.size) return null
    if (pt.x() < 0 || pt.x() >= this[pt.y()].size) return null
    return this[pt.y()][pt.x()]
}

inline fun <T : Any> parseFile(fileName: String, crossinline parsefn: (String) -> T): List<T> =
    ClassLoader.getSystemResourceAsStream(fileName).use { inputStream ->
        if (inputStream == null) throw RuntimeException("resource $fileName not found")
        inputStream.bufferedReader().lines().map { parsefn(it) }.toList()
    }

inline fun <T : Any> parseFileIndexed(fileName: String, crossinline parsefn: (Int, String) -> T): List<T> =
    ClassLoader.getSystemResourceAsStream(fileName).use { inputStream ->
        if (inputStream == null) throw RuntimeException("resource $fileName not found")
        inputStream.bufferedReader().lines().collect(Collectors.toList()).mapIndexed(parsefn).toList()
    }

inline fun <T : Any> parseLine(fileName: String, crossinline parsefn: (String) -> T): T =
    ClassLoader.getSystemResourceAsStream(fileName).use { inputStream ->
        if (inputStream == null) throw RuntimeException("resource $fileName not found")
        inputStream.bufferedReader().lines().map { parsefn(it) }.findFirst()
    }.get()

//fun <T> cartesian(c1: Collection<T>, c2: Collection<T> = c1): List<Pair<T, T>> =
//    c1.flatMap { a -> c2.map { b -> Pair(a, b) } }

fun <T> cartesian(c1: Iterable<T>, c2: Iterable<T> = c1): List<Pair<T, T>> =
    c1.flatMap { a -> c2.map { b -> Pair(a, b) } }

fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = runBlocking {
    map { async { f(it) } }.map { it.await() }
}

fun <A, B> Iterator<A>.pmap(f: suspend (A) -> B): List<B> {
    val sup = this
    return object : Iterable<A> {
        override fun iterator(): Iterator<A> {
            return sup
        }

    }.pmap(f)
}

fun <T, R> Array<T>.pmap(f: suspend (T) -> R) = runBlocking {
    map { async { f(it) } }.map { it.await() }
}

fun <A, B> Iterable<A>.pMapIndexed(f: suspend (Int, A) -> B): List<B> = runBlocking {
    mapIndexed { index, i -> async { f(index, i) } }.map { it.await() }
}


fun <T, R> Array<out T>.pMapIndexed(f: (index: Int, T) -> R): List<R> = runBlocking {
    mapIndexed { index, i -> async { f(index, i) } }.map { it.await() }
}

fun gcd(number1: Long, number2: Long): Long {
    return if (number2 == 0L) {
        number1
    } else gcd(number2, number1 % number2)
}

fun lcm(number1: Long, number2: Long): Long =
    (number1 / gcd(number1, number2)) * number2