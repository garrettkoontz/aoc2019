package com.k00ntz.aoc2019.utils

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.math.sqrt
import kotlin.streams.toList
import kotlin.system.measureTimeMillis

fun measureAndPrintTime(block: () -> Unit){
    val time = measureTimeMillis (block)
    println(" took $time ms")
}

inline fun <T : Any> parseFile(fileName: String, crossinline parsefn: (String) -> T): List<T> =
    ClassLoader.getSystemResourceAsStream(fileName).use {
        it.bufferedReader().lines().map { parsefn(it) }.toList()
    }

inline fun <T : Any> parseLine(fileName: String, crossinline parsefn: (String) -> T): T =
    ClassLoader.getSystemResourceAsStream(fileName).use {
        it.bufferedReader().lines().map { parsefn(it) }.findFirst()
    }.get()

//fun <T> cartesian(c1: Collection<T>, c2: Collection<T> = c1): List<Pair<T, T>> =
//    c1.flatMap { a -> c2.map { b -> Pair(a, b) } }

fun <T> cartesian(c1: Iterable<T>, c2: Iterable<T> = c1): List<Pair<T, T>> =
    c1.flatMap { a -> c2.map { b -> Pair(a, b) } }

typealias Point = Pair<Int, Int>

operator fun Point.plus(other: Point): Point =
    Pair(this.first + other.first, this.second + other.second)

fun Point.distanceTo(point: Point): Double {
    val xDiff = (this.x() - point.x()).toDouble()
    val yDiff = (this.y() - point.y()).toDouble()
    return sqrt(xDiff * xDiff + yDiff * yDiff)
}

fun Point.x(): Int =
    this.first

fun Point.y(): Int =
    this.second


fun ccw(p1: Point, p2: Point, p3: Point) =
    (p2.x() - p1.x()) * (p3.y() - p1.y()) - (p2.y() - p1.y()) * (p3.x() - p1.x())

fun convexHull(pts: List<Point>): List<Point> {
    val n = pts.size
    val minPt = pts.minBy { it.second }!!
    val parts = pts.partition { it.y() == minPt.y() }
    val others =
        parts.second.sortedBy { (minPt.x().toDouble() - it.x().toDouble()) / (it.y().toDouble() - minPt.y().toDouble()) }
    val points = listOf(minPt) + parts.first.filter { it.x() - minPt.x() > 0 } +
            others + parts.first.filter { it.x() - minPt.x() < 0 }
    val stack = Stack<Point>()
    stack.push(points[0])
    stack.push(points[1])
    for (i in (2 until n)) {
        while (stack.size >= 2 &&
            ccw(stack.elementAt(stack.size - 2), stack.peek(), points[i]) <= 0
        ) {
            stack.pop()
        }
        stack.push(points[i])

    }
    return stack.toList()
}

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

private fun <T> Array<T>.pmap(f: suspend (T) -> Unit) = runBlocking {
    map { async { f(it) } }.map { it.await() }
}

fun <A,B> Iterable<A>.pMapIndexed(f: suspend (Int, A) -> B): List<B> = runBlocking {
    mapIndexed { index, i -> async { f(index, i) }}.map {it.await()}
}


fun <T, R> Array<out T>.pMapIndexed(f: (index: Int, T) -> R): List<R> = runBlocking {
    mapIndexed { index, i -> async { f(index, i) }}.map {it.await()}
}