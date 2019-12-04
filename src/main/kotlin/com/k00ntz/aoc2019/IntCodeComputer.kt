package com.k00ntz.aoc2019

class IntCodeComputer(private val inputMemory: IntArray) {

    fun executeProgram(noun: Int? = null, verb: Int? = null): IntArray {
        val memory: IntArray = inputMemory.clone()
        memory[1] = noun ?: memory[1]
        memory[2] = verb ?: memory[2]
        var ip = 0
        var ins = memory.parseInstruction(ip)
        while (ins !is HLT) {
            ip = memory.executeInstruction(ins, ip)
            ins = memory.parseInstruction(ip)
        }
        return memory
    }
}

internal fun IntArray.executeInstruction(ins: Instruction, ip: Int): Int =
    ins.executeInstruction(this, ip)

internal fun IntArray.parseInstruction(ip: Int): Instruction =
    when (this[ip]) {
        1 -> Add(this[ip + 1], this[ip + 2], this[ip + 3])
        2 -> Mul(this[ip + 1], this[ip + 2], this[ip + 3])
        99 -> HLT()
        else -> {
            throw RuntimeException("unable to find opcode for int ${this[ip]}")
        }
    }

internal interface Instruction {
    fun executeInstruction(memory: IntArray, ip: Int): Int
}

internal class Add(val p1: Int, val p2: Int, val out: Int) : Instruction {
    override fun executeInstruction(memory: IntArray, ip: Int): Int {
        memory[out] = memory[p2] + memory[p1]
        return ip + 4
    }
}

internal class Mul(val p1: Int, val p2: Int, val out: Int) : Instruction {
    override fun executeInstruction(memory: IntArray, ip: Int): Int {
        memory[out] = memory[p2] * memory[p1]
        return ip + 4
    }
}

internal class HLT : Instruction {
    override fun executeInstruction(memory: IntArray, ip: Int): Int {
        return ip + 1
    }
}