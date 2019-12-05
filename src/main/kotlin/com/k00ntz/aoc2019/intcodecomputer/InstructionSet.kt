package com.k00ntz.aoc2019.intcodecomputer

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
        4 -> OutputIns(
            this[ip + 1],
            getMode((this[ip] / 100) % 10)
        )
        5 -> JT(
            this[ip + 1],
            getMode((this[ip] / 100) % 10),
            this[ip + 2],
            getMode((this[ip] / 1000) % 10)
        )
        6 -> JF(
            this[ip + 1],
            getMode((this[ip] / 100) % 10),
            this[ip + 2],
            getMode((this[ip] / 1000) % 10)
        )
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

enum class Mode(private val code: Int) {
    POSITION(0),
    IMMEDIATE(1);

    fun getValue(memory: IntArray, i: Int) =
        when (this) {
            POSITION -> memory[i]
            IMMEDIATE -> i
        }

}

fun getMode(i: Int): Mode =
    when (i) {
        1 -> Mode.IMMEDIATE
        else -> Mode.POSITION
    }

sealed class Instruction(val instructionSize: Int) {
    abstract fun executeInstruction(memory: IntArray, ip: Int, input: Input? = null, output: Output): Int
}

internal class JT(private val p1: Int, private val p1Mode: Mode, private val p2: Int, private val p2Mode: Mode) :
    Instruction(3) {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        return if (p1Mode.getValue(memory, p1) != 0) p2Mode.getValue(memory, p2) else ip + instructionSize
    }
}

internal class JF(private val p1: Int, private val p1Mode: Mode, private val p2: Int, private val p2Mode: Mode) :
    Instruction(3) {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int =
        if (p1Mode.getValue(memory, p1) == 0) p2Mode.getValue(memory, p2) else ip + instructionSize

}

internal class LT(private val p1: Int, private val p1Mode: Mode, private val p2: Int, private val p2Mode: Mode, private val p3: Int) :
    Instruction(4) {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        memory[p3] = if (p1Mode.getValue(memory, p1) < p2Mode.getValue(memory, p2)) 1 else 0
        return ip + instructionSize
    }
}

internal class EQ(private val p1: Int, private val p1Mode: Mode, private val p2: Int, private val p2Mode: Mode, private val p3: Int) :
    Instruction(4) {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        memory[p3] = if (p1Mode.getValue(memory, p1) == p2Mode.getValue(memory, p2)) 1 else 0
        return ip + instructionSize
    }
}

internal class InputIns(private val p1: Int) : Instruction(2) {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        if (input == null) throw RuntimeException("Null input!")
        else {
            memory[p1] = input.value
        }
        return ip + instructionSize
    }

}

internal class OutputIns(private val p1: Int, private val mode: Mode) :
    Instruction(2) {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        output.values.add(mode.getValue(memory, p1))
        return ip + instructionSize
    }

}

internal class Add(private val p1: Int, private val p1Mode: Mode, private val p2: Int, private val p2Mode: Mode, private val out: Int) :
    Instruction(4) {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        memory[out] = p2Mode.getValue(memory, p2) + p1Mode.getValue(memory, p1)
        return ip + instructionSize
    }
}

internal class Mul(private val p1: Int, private val p1Mode: Mode, private val p2: Int, private val p2Mode: Mode, private val out: Int) :
    Instruction(4) {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        memory[out] = p2Mode.getValue(memory, p2) * p1Mode.getValue(memory, p1)
        return ip + instructionSize
    }
}

internal class HLT : Instruction(1) {
    override fun executeInstruction(memory: IntArray, ip: Int, input: Input?, output: Output): Int {
        return ip + instructionSize
    }
}