package com.k00ntz.aoc.utils

class LinkedNode<T>(val value: T) {

    var prevNode: LinkedNode<T> = this
    var nextNode: LinkedNode<T> = this

    constructor(value: T, prevNode: LinkedNode<T>, nextNode: LinkedNode<T>) : this(value) {
        this.nextNode = nextNode
        this.prevNode = prevNode
    }

    fun nodeAt(distance: Int): LinkedNode<T> {
        return when {
            distance == 1 -> nextNode
            distance == -1 -> prevNode
            distance < 0 -> {
                (0 until Math.abs(distance)).fold(this) { acc, _ -> acc.prevNode }
            }
            distance > 0 -> {
                (0 until distance).fold(this) { acc, _ -> acc.nextNode }
            }
            else -> this
        }
    }

    fun removeAt(distance: Int): Pair<LinkedNode<T>, LinkedNode<T>> {
        val removedNode = nodeAt(distance)
        removedNode.prevNode.nextNode = removedNode.nextNode
        removedNode.nextNode.prevNode = removedNode.prevNode
        return Pair(removedNode, removedNode.nextNode)
    }

    fun add(i: T): LinkedNode<T> {
        val ln = LinkedNode(i)
        this.nextNode.prevNode = ln
        this.nextNode = ln
        return ln
    }

    fun toList(): List<T> {
        var node = this
        val lst = mutableListOf(this.value)
        while (node.nextNode != this) {
            node = node.nextNode
            lst.add(node.value)
        }
        return lst
    }

    override fun toString(): String {
        return this.toList().toString()
    }


}