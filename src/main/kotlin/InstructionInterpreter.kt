interface InstructionInterpreter {
    /**
     * Map a pair of bytes into the corresponding instruction class.
     * Gameboy instructions are either:
     * (1) A single byte (in which case the second byte is ignored)
     * (2) A pair of bytes in the case of a prefixed instruction. This prefix is always 0xCB
     */
    fun interpret(bytes: Pair<UByte, UByte>): Instruction

    companion object {
        val BIT16_OPCODE_PREFIX: UByte = 0xCBu
    }
}

class NOOPInstructionInterpreter : InstructionInterpreter {
    override fun interpret(bytes: Pair<UByte, UByte>): Instruction {
        return NOOPInstruction()
    }
}