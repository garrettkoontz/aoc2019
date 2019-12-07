package com.k00ntz.aoc2019.intcodecomputer

class WiredIntCodeComputer(private val inputMemory: IntArray, val input: Input, val output: Output) : Runnable {

    override fun run() {
        val memory: IntArray = inputMemory.clone()
        var ip = 0
        var ins = memory.parseInstruction(ip)
        while (ins !is HLT) {
            ip = memory.executeInstruction(ins, ip, input, output)
            ins = memory.parseInstruction(ip)
        }
    }
}

open class IntCodeComputer(private val inputMemory: IntArray) {

    var noun: Int? = null
    var verb: Int? = null

    open fun executeProgram(vararg input: Int): Pair<IntArray, List<Int>> {
        val memory: IntArray = inputMemory.clone()
        memory[1] = noun ?: memory[1]
        memory[2] = verb ?: memory[2]
        var ip = 0
        var ins = memory.parseInstruction(ip)
        val inp = if (input.isNotEmpty()) FixedInput(input.toList()) else null
        val output = FixedOutput()
        while (ins !is HLT) {
            ip = memory.executeInstruction(ins, ip, inp, output)
            ins = memory.parseInstruction(ip)
        }
        return Pair(memory, output.values)
    }
}

