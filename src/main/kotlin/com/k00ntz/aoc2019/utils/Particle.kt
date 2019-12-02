package com.k00ntz.aoc.utils

import com.k00ntz.aoc2019.utils.*


data class Particle(
    val position: Point = Pair(0, 0),
    val velocity: Point = Pair(0, 0),
    val acceleration: Point = Pair(0, 0)
) {
    fun move(): Particle {
        return Particle(position + velocity, velocity + acceleration, acceleration)
    }

    fun distanceTo(particle: Particle): Double {
        return position.distanceTo(particle.position)
    }

    val x = position.x()

    val y = position.y()

}


class ParticleGrid(val grid: Map<Int, List<Particle>>) {

    constructor(particles: List<Particle>) : this(particles.groupBy { it.x }
        .mapValues {
            it.value.sortedWith(Comparator { o1, o2 ->
                o1.y.compareTo(o2.y)
            })
        })

    fun tick(): ParticleGrid =
        ParticleGrid(grid.values.flatMap {
            it.pmap { it.move() }
        })


    // grid is a y indexed map to particles

    val pointChar: Char = '#'
    val notPointChar: Char = '.'


    fun containsRunGreaterThan(textsize: Int): Boolean {
        return grid.values
            .parallelStream()
            .filter { it.size >= textsize }.anyMatch { pm ->
                (0..pm.size - textsize).any {
                    val slice = pm.subList(it, it + textsize)
                    slice.zipWithNext().all { (p1, p2) ->
                        p1.y == p2.y - 1
                    }
                }
            }
    }

    override fun toString(): String {
        val maxY: Int = grid.keys.max()!!
        val maxX: Int = grid.map { it.value }.flatten().maxBy { it.position.x() }!!.position.x()
        val origin = Point(maxX, maxY)
        fun scaleParticle(pt: Particle): Point = origin + pt.position
        val arrays = (-maxY..maxY).map { (-maxX..maxX).map { notPointChar }.toTypedArray() }.toTypedArray()
        grid.forEach {
            it.value.pmap {
                val pos = scaleParticle(it)
                arrays[pos.y()][pos.x()] = pointChar
            }
        }
        return "0".padStart(6 + (maxX * 2), ' ') + "\n" +
                arrays.mapIndexed { index, chars ->
                    chars.joinToString(
                        separator = " ",
                        prefix = "${(index - maxY)}".padEnd(5, ' ')
                    )
                }
                    .joinToString(separator = "\n")
    }
}