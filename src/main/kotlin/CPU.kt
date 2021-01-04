import java.lang.Thread.sleep

class CPU(private val registers: Registers,
          private val memory: Memory,
          private val instructionInterpreter: InstructionInterpreter) {

    fun fetchAndRunNextInstruction() {
        val instructionAddress = registers.PC
        println("Fetching instruction at address $instructionAddress")
        val opcodeByteOne = memory.getByte(instructionAddress)
        // FIXME: This is wrong -- the same bit is included in both opcode && immediate data
        val immediateData = listOf(
            memory.getByte((instructionAddress + 1u).toUShort()),
            memory.getByte((instructionAddress + 2u).toUShort()),
            memory.getByte((instructionAddress + 3u).toUShort())
        )
        val opcode = Pair(opcodeByteOne, immediateData.get(0))
        execInstruction(opcode, immediateData)
        sleep(10)
    }

    fun execInstruction(opcode: Pair<UByte, UByte>, immediateData: Collection<UByte>) {
        instructionInterpreter.interpret(opcode).also { instruction ->
            println("Executing instruction: ${opcode} / ${instruction.javaClass}")
            registers.PC = (registers.PC + instruction.size).toUShort()
            instruction.invoke(registers, memory, immediateData)
            println("Register state -> ${registers.dumpRegisters()}")
        }
    }
}