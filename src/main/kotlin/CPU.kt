import java.lang.Thread.sleep

class CPU(private val registers: Registers,
          private val memory: Memory,
          private val instructionInterpreter: InstructionInterpreter,
          private val continueOnFatals: Boolean? = false,
          private val verbose: Boolean? = true) {

    private var cycleCount: Long = 0

    fun fetchAndRunNextInstruction() {
        ++cycleCount
        val instructionAddress = registers.PC()
        // println("Cycle ${cycleCount}: Fetching instruction at address $instructionAddress")
        val data = listOf(
            memory.getByte((instructionAddress).toUShort()),
            memory.getByte((instructionAddress + 1u).toUShort()),
            memory.getByte((instructionAddress + 2u).toUShort()),
            memory.getByte((instructionAddress + 3u).toUShort())
        )
        execInstruction(data)
    }

    fun execInstruction(bytes: List<UByte>) {
        assert(4 == bytes.size)
        val opcode = Pair(bytes[0], bytes[1])
        try {
            instructionInterpreter.interpret(opcode).also { instruction ->
                println("Cycle $cycleCount: Executing instruction ${instruction.javaClass} / ${bytes.toHexString()}")
                registers.setPC(
                    (registers.PC() + instruction.size).toUShort())
                instruction.invoke(registers, memory, bytes)
                println("Cycle $cycleCount: Register state -> ${registers.dumpRegisters()}")
            }
        } catch(x: Exception) {
            if (true == this.continueOnFatals) {
                println(x)
                registers.setPC(
                    (registers.PC().inc()))
            } else {
                if (true == verbose) {
                    println("Cycle $cycleCount: failed to execute at address ${registers.PC().toHexString()}:: ${bytes.toHexString()}")
                    println("Register state -> ${registers.dumpRegisters()}")
                }
                throw x
            }
        }
    }
}