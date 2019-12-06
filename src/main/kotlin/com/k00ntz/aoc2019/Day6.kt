package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import com.k00ntz.aoc2019.utils.parseFile

fun main() {
    Day6().run()
}

class Day6 : Day {
    override fun run() {
        val input = parseFile("day6.txt") { it.split(")").let { Pair(it[0], it[1]) } }
        val (graph, com) = buildGraph(input)
        measureAndPrintTime {
            print(countOrbits(com))
        }
        measureAndPrintTime { print(minTransfers(graph.findNode("YOU")!!, graph.findNode("SAN")!!)) }
    }

    fun buildGraph(links: List<Pair<String, String>>): Pair<DirectedGraph<String>, Node<String>> {
        val graph: DirectedGraph<String> = DirectedGraph()
        val comNode = graph.addNode("COM")
        links.forEach { graph.addFromTo(it.first, it.second) }
        return Pair(graph, comNode)
    }

    fun countOrbits(headNode: Node<String>): Int {
        var nextLevel = headNode.children.toList()
        var countOrbits = nextLevel.size
        var i = 1
        while (nextLevel.isNotEmpty()) {
            nextLevel = nextLevel.flatMap { it.children }.toList()
            countOrbits += nextLevel.size * ++i
        }
        return countOrbits
    }

    fun minTransfers(from: Node<String>, to: Node<String>): Int {
        val toParents = to.parents.toSet()
        val seen = from.parents.toMutableSet()
        var newFrom = from.parents.toList()
        var i = 0
        while (newFrom.toSet().intersect(toParents).isEmpty()) {
            seen.addAll(newFrom)
            i++
            newFrom = newFrom.flatMap { it.parents.plus(it.children) }.minus(seen)
        }
        return i
    }
}

class DirectedGraph<T>(private val nodes: MutableMap<T, Node<T>> = mutableMapOf()) {
    fun addNode(t: T): Node<T> =
        Node(t).also {
            nodes[t] = it
        }


    fun addFromTo(t1: T, t2: T) {
        val node1 = nodes[t1] ?: addNode(t1)
        val node2 = nodes[t2] ?: addNode(t2)
        node1.children.add(node2)
        node2.parents.add(node1)
    }

    fun findNode(t: T): Node<T>? =
        nodes[t]

}

class Node<T>(
    val value: T,
    val children: MutableList<Node<T>> = mutableListOf(),
    val parents: MutableList<Node<T>> = mutableListOf()
) {
    override fun toString(): String {
        return "Node(value=$value)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node<*>

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }

}