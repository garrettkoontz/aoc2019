package com.k00ntz.aoc2019.utils

typealias Point3 = Triple<Int, Int, Int>

fun Point3.x() = this.first
fun Point3.y() = this.second
fun Point3.z() = this.third

operator fun Point3.plus(other: Point3): Point3 =
    Point3(this.x() + other.x(), this.y() + other.y(), this.z() + other.z())
