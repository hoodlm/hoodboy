interface InstructionInterpreter {
    fun interpret(byte: UByte): Instruction
}

class NOOPInstructionInterpreter : InstructionInterpreter {
    override fun interpret(byte: UByte): Instruction {
        return Instruction.NOOP
    }
}