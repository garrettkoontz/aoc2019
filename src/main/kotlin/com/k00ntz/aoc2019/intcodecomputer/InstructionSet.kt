package com.k00ntz.aoc2019.intcodecomputer

import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.atomic.AtomicInteger

internal fun MutableList<Long>.executeInstruction(
    ins: Instruction,
    ip: Int,
    input: Input?,
    output: Output,
    relativeBaseBox: RelativeBase
): Int =
    ins.executeInstruction(this, ip, input, output, relativeBaseBox)

internal fun MutableList<Long>.parseInstruction(ip: Int): Instruction =
    when ((this[ip] % 100).toInt()) {
        1 -> Add(
            this[ip + 1],
            getMode((this[ip] / 100) % 10),
            this[ip + 2],
            getMode((this[ip] / 1000) % 10),
            this[ip + 3].toInt(),
            getMode((this[ip] / 10000) % 10)
        )
        2 -> Mul(
            this[ip + 1],
            getMode((this[ip] / 100) % 10),
            this[ip + 2],
            getMode((this[ip] / 1000) % 10),
            this[ip + 3].toInt(),
            getMode((this[ip] / 10000) % 10)
        )
        3 -> InputIns(
            this[ip + 1].toInt(),
            getMode((this[ip] / 100) % 10)
        )
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
            this[ip + 3].toInt(),
            getMode((this[ip] / 10000) % 10)
        )
        8 -> EQ(
            this[ip + 1],
            getMode((this[ip] / 100) % 10),
            this[ip + 2],
            getMode((this[ip] / 1000) % 10),
            this[ip + 3].toInt(),
            getMode((this[ip] / 10000) % 10)
        )
        9 -> ARB(
            this[ip + 1],
            getMode((this[ip] / 100) % 10)
        )
        99 -> HLT()
        else -> {
            throw RuntimeException("unable to find opcode for int ${this[ip]} at $ip")
        }
    }

interface Input {
    fun get(): Long
}

interface Output {
    fun send(i: Long)
}

data class IOBuffer(
    val initialInput: List<Long> = emptyList(),
    val buffer: LinkedBlockingDeque<Long> = LinkedBlockingDeque(initialInput),
    val awaiting: AtomicInteger = AtomicInteger(0)
) : Input,
    Output {
    override fun get(): Long {
        return if (buffer.peek() == null)
            awaiting.incrementAndGet()
                .let { buffer.take() }
                .also {
                    awaiting.decrementAndGet()
                } else buffer.take()
    }

    override fun send(i: Long) {
        if (buffer.peekLast() == 22L && i == 3L)
            println("putting 3")
        buffer.put(i)
    }
}

data class FixedOutput(val values: MutableList<Long> = mutableListOf()) : Output {
    override fun send(i: Long) {
        values.add(i)
    }
}

data class FixedInput(val value: List<Long>, private var state: Int = 0) : Input {
    override fun get(): Long {
        return value[state++]
    }
}

data class RelativeBase(var rb: Int = 0)

enum class Mode(private val code: Int) {
    POSITION(0),
    IMMEDIATE(1),
    RELATIVE(2);

    fun getValue(memory: MutableList<Long>, i: Long, relativeBaseBox: RelativeBase) =
        when (this) {
            POSITION -> memory.getOrNull(i.toInt()) ?: 0
            IMMEDIATE -> i
            RELATIVE -> memory.getOrNull(i.toInt() + relativeBaseBox.rb) ?: 0
        }

    fun getOutputLocation(out: Int, relativeBaseBox: RelativeBase) =
        when (this) {
            POSITION -> out
            RELATIVE -> out + relativeBaseBox.rb
            IMMEDIATE -> throw RuntimeException("unable to use IMMEDIATE type as output location.")
        }.toInt()
}

fun getMode(i: Long): Mode =
    when (i) {
        0L -> Mode.POSITION
        1L -> Mode.IMMEDIATE
        2L -> Mode.RELATIVE
        else -> throw RuntimeException("unable to determine mode for $i")
    }

sealed class Instruction(val instructionSize: Int) {
    abstract fun executeInstruction(
        memory: MutableList<Long>,
        ip: Int,
        input: Input? = null,
        output: Output,
        relativeBaseBox: RelativeBase
    ): Int

    fun nextInsPointer(ip: Int) = ip + instructionSize

}


internal abstract class WriteInstruction(instructionSize: Int, val out: Int, val outMode: Mode) :
    Instruction(instructionSize) {

    fun ensureSize(memory: MutableList<Long>, requireSize: Int, defaultValue: Long = 0) {
        if (memory.size <= requireSize) {
            memory.addAll(List(requireSize - memory.size + 1) { defaultValue })
        }
    }

    abstract fun executeWriteInstruction(
        memory: MutableList<Long>,
        ip: Int,
        input: Input? = null,
        output: Output,
        relativeBaseBox: RelativeBase
    )

    override fun executeInstruction(
        memory: MutableList<Long>,
        ip: Int,
        input: Input?,
        output: Output,
        relativeBaseBox: RelativeBase
    ): Int {
        ensureSize(memory, outMode.getOutputLocation(out, relativeBaseBox))
        executeWriteInstruction(memory, ip, input, output, relativeBaseBox)
        return nextInsPointer(ip)
    }

}

internal class JT(private val p1: Long, private val p1Mode: Mode, private val p2: Long, private val p2Mode: Mode) :
    Instruction(3) {
    override fun executeInstruction(
        memory: MutableList<Long>,
        ip: Int,
        input: Input?,
        output: Output,
        relativeBaseBox: RelativeBase
    ): Int = if (p1Mode.getValue(memory, p1, relativeBaseBox) != 0L) p2Mode.getValue(
        memory,
        p2,
        relativeBaseBox
    ).toInt() else ip + instructionSize

    override fun toString(): String {
        return "JT(p1=$p1, p1Mode=$p1Mode, p2=$p2, p2Mode=$p2Mode)"
    }
}

internal class JF(private val p1: Long, private val p1Mode: Mode, private val p2: Long, private val p2Mode: Mode) :
    Instruction(3) {
    override fun executeInstruction(
        memory: MutableList<Long>,
        ip: Int,
        input: Input?,
        output: Output,
        relativeBaseBox: RelativeBase
    ): Int =
        if (p1Mode.getValue(memory, p1, relativeBaseBox) == 0L) p2Mode.getValue(
            memory,
            p2,
            relativeBaseBox
        ).toInt() else ip + instructionSize

    override fun toString(): String {
        return "JF(p1=$p1, p1Mode=$p1Mode, p2=$p2, p2Mode=$p2Mode)"
    }

}

internal class LT(
    private val p1: Long,
    private val p1Mode: Mode,
    private val p2: Long,
    private val p2Mode: Mode,
    out: Int,
    outMode: Mode
) :
    WriteInstruction(4, out, outMode) {
    override fun executeWriteInstruction(
        memory: MutableList<Long>,
        ip: Int,
        input: Input?,
        output: Output,
        relativeBaseBox: RelativeBase
    ) {
        memory[outMode.getOutputLocation(out, relativeBaseBox)] =
            if (p1Mode.getValue(memory, p1, relativeBaseBox) < p2Mode.getValue(memory, p2, relativeBaseBox)) 1 else 0
    }

    override fun toString(): String {
        return "LT(p1=$p1, p1Mode=$p1Mode, p2=$p2, p2Mode=$p2Mode, out: $out, outMode: $outMode)"
    }
}

internal class EQ(
    private val p1: Long,
    private val p1Mode: Mode,
    private val p2: Long,
    private val p2Mode: Mode,
    out: Int,
    outMode: Mode
) :
    WriteInstruction(4, out, outMode) {
    override fun executeWriteInstruction(
        memory: MutableList<Long>,
        ip: Int,
        input: Input?,
        output: Output,
        relativeBaseBox: RelativeBase
    ) {
        memory[outMode.getOutputLocation(out, relativeBaseBox)] =
            if (p1Mode.getValue(memory, p1, relativeBaseBox) == p2Mode.getValue(memory, p2, relativeBaseBox)) 1 else 0
    }

    override fun toString(): String {
        return "EQ(p1=$p1, p1Mode=$p1Mode, p2=$p2, p2Mode=$p2Mode, out: $out, outMode: $outMode)"
    }
}

internal class InputIns(out: Int, private val mode: Mode) : WriteInstruction(2, out, mode) {
    override fun executeWriteInstruction(
        memory: MutableList<Long>,
        ip: Int,
        input: Input?,
        output: Output,
        relativeBaseBox: RelativeBase
    ) {
        if (input == null) throw RuntimeException("Null input!")
        else {
            memory[mode.getOutputLocation(out, relativeBaseBox)] = input.get()
        }
    }

    override fun toString(): String {
        return "InputIns(out: $out, outMode: $outMode)"
    }

}

internal class OutputIns(private val p1: Long, private val mode: Mode) :
    Instruction(2) {
    override fun executeInstruction(
        memory: MutableList<Long>,
        ip: Int,
        input: Input?,
        output: Output,
        relativeBaseBox: RelativeBase
    ): Int {
        output.send(mode.getValue(memory, p1, relativeBaseBox))
        return ip + instructionSize
    }

    override fun toString(): String {
        return "OutputIns(p1=$p1, mode=$mode)"
    }

}

internal class Add(
    private val p1: Long,
    private val p1Mode: Mode,
    private val p2: Long,
    private val p2Mode: Mode,
    out: Int,
    outMode: Mode
) :
    WriteInstruction(4, out, outMode) {
    override fun executeWriteInstruction(
        memory: MutableList<Long>,
        ip: Int,
        input: Input?,
        output: Output,
        relativeBaseBox: RelativeBase
    ) {
        memory[outMode.getOutputLocation(out, relativeBaseBox)] =
            p2Mode.getValue(memory, p2, relativeBaseBox) + p1Mode.getValue(memory, p1, relativeBaseBox)
    }

    override fun toString(): String {
        return "Add(p1=$p1, p1Mode=$p1Mode, p2=$p2, p2Mode=$p2Mode, out: $out, outMode: $outMode)"
    }
}

internal class Mul(
    private val p1: Long,
    private val p1Mode: Mode,
    private val p2: Long,
    private val p2Mode: Mode,
    out: Int,
    outMode: Mode
) :
    WriteInstruction(4, out, outMode) {
    override fun executeWriteInstruction(
        memory: MutableList<Long>,
        ip: Int,
        input: Input?,
        output: Output,
        relativeBaseBox: RelativeBase
    ) {
        memory[outMode.getOutputLocation(out, relativeBaseBox)] =
            p2Mode.getValue(memory, p2, relativeBaseBox) * p1Mode.getValue(memory, p1, relativeBaseBox)
    }

    override fun toString(): String {
        return "Mul(p1=$p1, p1Mode=$p1Mode, p2=$p2, p2Mode=$p2Mode, out: $out, outMode: $outMode)"
    }
}

internal class ARB(private val p1: Long, private val p1Mode: Mode) : Instruction(2) {
    override fun executeInstruction(
        memory: MutableList<Long>,
        ip: Int,
        input: Input?,
        output: Output,
        relativeBaseBox: RelativeBase
    ): Int {
        relativeBaseBox.rb += p1Mode.getValue(memory, p1, relativeBaseBox).toInt()
        return ip + instructionSize
    }

    override fun toString(): String {
        return "ARB(p1=$p1, p1Mode=$p1Mode)"
    }

}

internal class HLT : Instruction(1) {
    override fun executeInstruction(
        memory: MutableList<Long>,
        ip: Int,
        input: Input?,
        output: Output,
        relativeBaseBox: RelativeBase
    ): Int {
        return ip + instructionSize
    }

    override fun toString(): String {
        return "HLT()"
    }


}