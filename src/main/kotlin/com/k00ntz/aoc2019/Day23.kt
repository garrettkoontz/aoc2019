package com.k00ntz.aoc2019

import com.k00ntz.aoc2019.intcodecomputer.Input
import com.k00ntz.aoc2019.intcodecomputer.Output
import com.k00ntz.aoc2019.intcodecomputer.YieldingIntCodeComputer
import com.k00ntz.aoc2019.intcodecomputer.parseIntComputerInput
import com.k00ntz.aoc2019.utils.Day
import com.k00ntz.aoc2019.utils.measureAndPrintTime
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

fun main() {
    Day23().run()
}

class Day23 : Day {
    override fun run() {
        val input = parseIntComputerInput(("${this::class.simpleName!!.toLowerCase()}.txt"))
        measureAndPrintTime {
            print(part1(input))
        }
        measureAndPrintTime {
            print(part2(input))
        }
    }

    private fun part2(input: LongArray): Long? {
        val input255 = NICInput(object : LinkedBlockingQueue<Long>() {
            override fun add(element: Long): Boolean {
//                println("receiving $element")
                if (this.size > 1) this.clear()
                return super.add(element)
            }
        })
        val delivered = mutableSetOf<Long>()
        val inputs = (0..49).associateWith { NICInput(LinkedBlockingQueue(listOf(it.toLong()))) }
        val machines = inputs.map { YieldingIntCodeComputer(input, it.value, NICOutput(inputs, input255 = input255)) }
        var idleCount = 0
        val threshold = 1000
        while (true) {
            if (idleCount > threshold) {
                val point = Pair(input255.get(), input255.get())
                if (delivered.contains(point.second))
                    return point.second
                else delivered.add(point.second)
//                println("Delivering $point")
                inputs[0]?.queue?.offer(point.first)
                inputs[0]?.queue?.offer(point.second)
                idleCount = 0
            }
            machines.forEach { it.step() }
            if (inputs.all { it.value.queue.isEmpty() }) {
                idleCount++
            } else {
                idleCount = 0
            }
        }
    }

    private fun part1(input: LongArray): Long {
        val output255 = NICInput()
        val inputs = (0..49).associateWith { NICInput(LinkedBlockingQueue(listOf(it.toLong()))) }
            .plus(Pair(255, output255))
        val machines =
            inputs.filter { it.key != 255 }
                .map { YieldingIntCodeComputer(input, it.value, NICOutput(inputs, input255 = output255)) }
        var stepCounter = 0
        while (output255.queue.size < 2) {
            machines.forEach { it.step() }
            stepCounter++
        }
//        println(stepCounter)
        return output255.queue.last()
    }

    class NICInput(val queue: Queue<Long> = LinkedBlockingQueue()) : Input {
        override fun get(): Long {
            val returnVal = queue.poll() ?: -1
            return returnVal
        }
    }

    class NICOutput(
        val inputs: Map<Int, NICInput>,
        val size: Int = 3,
        var start: Int = 0,
        private val input255: NICInput
    ) : Output {
        lateinit var nicInput: NICInput

        override fun send(i: Long) {
//            println("Returning $i from ${Thread.currentThread().name}")
            when (start) {
                0 -> nicInput = inputs.getOrDefault(i.toInt(), input255)
                else -> nicInput.queue.add(i)
            }
            start++
            start %= size
        }

    }
}