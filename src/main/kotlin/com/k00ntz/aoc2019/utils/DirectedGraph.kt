package com.k00ntz.aoc2019.utils

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