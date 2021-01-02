class CPU(private val registers: Registers,
          private val memory: Memory,
          private val instructionInterpreter: InstructionInterpreter) {

    fun execInstruction(instruction: UByte) {
        registers.PC = registers.PC.inc()
        instructionInterpreter.interpret(instruction).invoke()
    }
}