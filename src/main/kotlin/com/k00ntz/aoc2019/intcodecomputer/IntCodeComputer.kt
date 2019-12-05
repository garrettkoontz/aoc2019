package com.k00ntz.aoc2019.intcodecomputer

class IntCodeComputer(private val inputMemory: IntArray) {

    fun executeProgram(noun: Int? = null, verb: Int? = null, input: Int? = null): Pair<IntArray, List<Int>> {
        val memory: IntArray = inputMemory.clone()
        memory[1] = noun ?: memory[1]
        memory[2] = verb ?: memory[2]
        var ip = 0
        var ins = memory.parseInstruction(ip)
        val inp = input?.let { Input(input) }
        val output = Output()
        while (ins !is HLT) {
            ip = memory.executeInstruction(ins, ip, inp, output)
            ins = memory.parseInstruction(ip)
        }
        return Pair(memory, output.values)
    }
}

