package com.k00ntz.aoc2019

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

internal fun IntArray.executeInstruction(ins: Instruction, ip: Int, input: Input?, output: Output): Int =
    ins.executeInstruction(this, ip, input, output)

internal fun IntArray.parseInstruction(ip: Int): Instruction =
    when (this[ip] % 100) {
        1 -> Add(
            this[ip + 1],
            getMode((this[ip] / 100) % 10),
            this[ip + 2],
            getMode((this[ip] / 1000) % 10),
            this[ip + 3]
        )
        2 -> Mul(
            this[ip + 1],
            getMode((this[ip] / 100) % 10),
            this[ip + 2],
            getMode((this[ip] / 1000) % 10),
            this[ip + 3]
        )
        3 -> InputIns(this[ip + 1])
        4 -> OutputIns(this[ip + 1], getMode((this[ip] / 100) % 10))
        5 -> JT(this[ip + 1], getMode((this[ip] / 100) % 10), this[ip + 2], getMode((this[ip] / 1000) % 10))
        6 -> JF(this[ip + 1], getMode((this[ip] / 100) % 10), this[ip + 2], getMode((this[ip] / 1000) % 10))
        7 -> LT(
            this[ip + 1],
            getMode((this[ip] / 100) % 10),
            this[ip + 2],
            getMode((this[ip] / 1000) % 10),
            this[ip + 3]
        )
        8 -> EQ(
            this[ip + 1],
            getMode((this[ip] / 100) % 10),
            this[ip + 2],
            getMode((this[ip] / 1000) % 10),
            this[ip + 3]
        )
        99 -> HLT()
        else -> {
            throw RuntimeException("unable to find opcode for int ${this[ip]} at $ip")
        }
    }

data class Output(val values: MutableList<Int> = mutableListOf())
data class Input(val value: Int)

enum class Mode(val code: Int) {
    POSITION(0),
    IMMEDIATE(1);
}

fun getMode(i: Int): Mode =
    when (i) {
        1 -> Mode.IMMEDIATE
        else -> Mode.POSITION
    }

internal interface Instruction {
    fun executeInstruction(memory: IntArray, ip: Int, input: Input? = null, output: Output): Int
}

internal class JT(val p1: Int, val p1Mode: Mode, val p2: Int, val p2Mode: Mode) : Instruction {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        val param1 = if (p1Mode == Mode.POSITION) memory[p1] else p1
        val param2 = if (p2Mode == Mode.POSITION) memory[p2] else p2
        return if (param1 != 0) param2 else ip + 3
    }
}

internal class JF(val p1: Int, val p1Mode: Mode, val p2: Int, val p2Mode: Mode) : Instruction {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        val param1 = if (p1Mode == Mode.POSITION) memory[p1] else p1
        val param2 = if (p2Mode == Mode.POSITION) memory[p2] else p2
        return if (param1 == 0) param2 else ip + 3
    }
}

internal class LT(val p1: Int, val p1Mode: Mode, val p2: Int, val p2Mode: Mode, val p3: Int) : Instruction {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        val param1 = if (p1Mode == Mode.POSITION) memory[p1] else p1
        val param2 = if (p2Mode == Mode.POSITION) memory[p2] else p2
        memory[p3] = if (param1 < param2) 1 else 0
        return ip + 4
    }
}

internal class EQ(val p1: Int, val p1Mode: Mode, val p2: Int, val p2Mode: Mode, val p3: Int) : Instruction {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        val param1 = if (p1Mode == Mode.POSITION) memory[p1] else p1
        val param2 = if (p2Mode == Mode.POSITION) memory[p2] else p2
        memory[p3] = if (param1 == param2) 1 else 0
        return ip + 4
    }
}

internal class InputIns(val p1: Int) : Instruction {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        if (input == null) throw RuntimeException("Null input!")
        else {
            memory[p1] = input.value
        }
        return ip + 2
    }

}

internal class OutputIns(val p1: Int, val mode: Mode) : Instruction {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        when (mode) {
            Mode.POSITION -> output.values.add(memory[p1])
            Mode.IMMEDIATE -> output.values.add(p1)
        }
        return ip + 2
    }

}

internal class Add(val p1: Int, val p1Mode: Mode, val p2: Int, val p2Mode: Mode, val out: Int) : Instruction {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        val newVal =
            (if (p2Mode == Mode.POSITION) memory[p2] else p2) + (if (p1Mode == Mode.POSITION) memory[p1] else p1)
        memory[out] = newVal
        return ip + 4
    }
}

internal class Mul(val p1: Int, val p1Mode: Mode, val p2: Int, val p2Mode: Mode, val out: Int) : Instruction {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        memory[out] = (if (p2Mode == Mode.POSITION) memory[p2] else p2) *
                (if (p1Mode == Mode.POSITION) memory[p1] else p1)
        return ip + 4
    }
}

internal class HLT : Instruction {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        return ip + 1
    }
}