class CPU(val registers: Registers,
          val memory: Memory,
          val instructionInterpreter: InstructionInterpreter) {

    fun execInstruction(instruction: UByte) {
        instructionInterpreter.interpret(instruction).invoke()
    }
}