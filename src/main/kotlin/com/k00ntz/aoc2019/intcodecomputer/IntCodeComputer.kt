package com.k00ntz.aoc2019.intcodecomputer

class WiredIntCodeComputer(inputMemory: IntArray, val input: Input, val output: Output) :
    AbstractIntCodeComputer(inputMemory), Runnable {

    override fun run() {
        val memory: IntArray = inputMemory.clone()
        go(memory, input, output)
    }
}

abstract class AbstractIntCodeComputer(internal val inputMemory: IntArray) {
    open fun go(memory: IntArray, input: Input, output: Output) {
        var ip = 0
        var ins = memory.parseInstruction(ip)
        while (ins !is HLT) {
            ip = memory.executeInstruction(ins, ip, input, output)
            ins = memory.parseInstruction(ip)
        }
    }
}

open class IntCodeComputer(inputMemory: IntArray, val noun: Int? = null, val verb: Int? = null) : AbstractIntCodeComputer(inputMemory) {

    fun executeProgram(vararg input: Int): Pair<IntArray, List<Int>> {
        val memory: IntArray = inputMemory.clone()
        memory[1] = noun ?: memory[1]
        memory[2] = verb ?: memory[2]

        val inp = FixedInput(input.toList())
        val output = FixedOutput()
        go(memory, inp, output)
        return Pair(memory, output.values)
    }
}

