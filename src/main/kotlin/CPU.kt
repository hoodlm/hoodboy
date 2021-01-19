import java.lang.Thread.sleep

class CPU(private val registers: Registers,
          private val memory: Memory,
          private val instructionInterpreter: InstructionInterpreter,
          private val continueOnFatals: Boolean? = false) {

    fun fetchAndRunNextInstruction() {
        val instructionAddress = registers.PC
        println("Fetching instruction at address $instructionAddress")
        val data = listOf(
            memory.getByte((instructionAddress).toUShort()),
            memory.getByte((instructionAddress + 1u).toUShort()),
            memory.getByte((instructionAddress + 2u).toUShort()),
            memory.getByte((instructionAddress + 3u).toUShort())
        )
        execInstruction(data)
        sleep(10)
    }

    fun execInstruction(bytes: List<UByte>) {
        assert(4 == bytes.size)
        val opcode = Pair(bytes[0], bytes[1])
        try {
            instructionInterpreter.interpret(opcode).also { instruction ->
                println("Executing instruction: ${opcode} / ${instruction.javaClass}")
                registers.PC = (registers.PC + instruction.size).toUShort()
                instruction.invoke(registers, memory, bytes)
                println("Register state -> ${registers.dumpRegisters()}")
            }
        } catch(x: Exception) {
            if (true == this.continueOnFatals) {
                println(x)
                registers.PC = registers.PC.inc()
            } else {
                throw x
            }
        }
    }
}