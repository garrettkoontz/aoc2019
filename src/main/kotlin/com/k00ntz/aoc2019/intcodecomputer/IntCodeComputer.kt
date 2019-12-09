package com.k00ntz.aoc2019.intcodecomputer

class WiredIntCodeComputer(inputMemory: LongArray, val input: Input, val output: Output, debug: Boolean = false) :
    AbstractIntCodeComputer(inputMemory, debug), Runnable {

    override fun run() {
        val memory: MutableList<Long> = inputMemory.toMutableList()
        go(memory, input, output, RelativeBase())
    }
}

abstract class AbstractIntCodeComputer(internal val inputMemory: LongArray, val debug: Boolean = false) {
    open fun go(memory: MutableList<Long>, input: Input, output: Output, relativeBase: RelativeBase) {
        var ip = 0
        var ins = memory.parseInstruction(ip)
        while (ins !is HLT) {
            if (debug) {
                println("ip: $ip, val: ${memory[ip]}, ins: $ins")
            }
            ip = memory.executeInstruction(ins, ip, input, output, relativeBase)
            ins = memory.parseInstruction(ip)
        }
    }
}

open class IntCodeComputer(
    inputMemory: LongArray,
    val noun: Long? = null,
    val verb: Long? = null,
    debug: Boolean = false
) : AbstractIntCodeComputer(inputMemory, debug) {

    fun executeProgram(vararg input: Long): Pair<LongArray, List<Long>> {
        val memory: MutableList<Long> = inputMemory.toMutableList()
        memory[1] = noun ?: memory[1]
        memory[2] = verb ?: memory[2]

        val inp = FixedInput(input.toList())
        val output = FixedOutput()
        go(memory, inp, output, RelativeBase())
        return Pair(memory.toLongArray(), output.values)
    }

}


