class CPU(private val registers: Registers,
          private val memory: Memory,
          private val instructionInterpreter: InstructionInterpreter) {

    fun execInstruction(opcode: Pair<UByte, UByte>, immediateData: Collection<UByte>) {
        instructionInterpreter.interpret(opcode).also { instruction ->
            registers.PC = (registers.PC + instruction.size).toUShort()
            instruction.invoke(registers, memory, immediateData)
        }
    }
}