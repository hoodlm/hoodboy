import InstructionInterpreter.Companion.BIT16_OPCODE_PREFIX

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

/**
 * The real deal, derived from https://meganesulli.com/generate-gb-opcodes/
 */
class GameBoyInstructionInterpreter: InstructionInterpreter {
    override fun interpret(bytes: Pair<UByte, UByte>): Instruction {
        return if (BIT16_OPCODE_PREFIX == bytes.first) {
            interpret16BitOpCode(bytes.second)
        } else {
            interpret8BitOpCode(bytes.first)
        }
    }

    private fun interpret8BitOpCode(opcode: UByte): Instruction {
        return when (opcode.toUInt()) {
            0x00u -> InstructionNOOP()
            0x01u -> InstructionLoadBCd16()
            0x11u -> InstructionLoadDEd16()
            0x21u -> InstructionLoadHLd16()
            else -> {
                throw RuntimeException("8-bit opcode $opcode is not yet implemented")
            }
        }
    }

    private fun interpret16BitOpCode(opcode: UByte): Instruction {
        throw RuntimeException("16-bit opcode $BIT16_OPCODE_PREFIX $opcode is not yet implemented")
    }
}

/**
 * Stub, e.g. for testing
 */
class NOOPInstructionInterpreter: InstructionInterpreter {
    override fun interpret(bytes: Pair<UByte, UByte>): Instruction {
        return InstructionNOOP()
    }
}