package com.k00ntz.aoc2019.utils

import kotlin.math.abs

class Point3(val x: Int, val y: Int, val z: Int, val label: String = "x: $x, y: $y, z: $z") {

    companion object {
        private val r = "<x=(-?[0-9]+), y=(-?[0-9]+), z=(-?[0-9]+)>".toRegex()
        fun fromString(s: String): Point3 =
            r.matchEntire(s)?.destructured?.let { (x, y, z) ->
                Point3(x.toInt(), y.toInt(), z.toInt())
            } ?: throw RuntimeException("unable to parse $s to Point3")

    }

    operator fun plus(other: Point3): Point3 =
        Point3(this.x + other.x, this.y + other.y, this.z + other.z, this.label)

    override fun toString(): String {
        return "Point3(x=$x, y=$y, z=$z, label='$label')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point3) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }

    fun energy(): Int =
        abs(x) + abs(y) + abs(z)

}
