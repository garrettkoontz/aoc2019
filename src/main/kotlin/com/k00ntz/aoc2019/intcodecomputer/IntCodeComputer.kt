package com.k00ntz.aoc2019.intcodecomputer

import com.k00ntz.aoc2019.utils.parseFile

fun parseIntComputerInput(fileName: String): LongArray =
    parseFile(fileName) { it.split(",").map { it.toLong() }.toLongArray() }.first()

class WiredIntCodeComputer(
    inputMemory: LongArray,
    val input: Input,
    val output: Output,
    exitCode: Long = -1,
    debug: Boolean = false
) :
    AbstractIntCodeComputer(inputMemory, debug, exitCode), Runnable {

    override fun run() {
        val memory: MutableList<Long> = inputMemory.toMutableList()
        go(memory, input, output, RelativeBase())
    }
}

abstract class AbstractIntCodeComputer(
    internal val inputMemory: LongArray,
    val debug: Boolean = false,
    val exitCode: Long = -1
) {
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
        output.send(exitCode)
    }
}

open class IntCodeComputer(
    inputMemory: LongArray,
    val noun: Long? = null,
    val verb: Long? = null,
    debug: Boolean = false,
    exitCode: Long = -1
) : AbstractIntCodeComputer(inputMemory, debug, exitCode) {

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


