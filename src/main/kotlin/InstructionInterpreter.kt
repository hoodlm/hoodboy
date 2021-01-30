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
            0x03u -> InstructionIncrementBC()
            0x04u -> InstructionIncrementB()
            0x05u -> InstructionDecrementB()
            0x06u -> InstructionLoadBd8()
            0x0Bu -> InstructionDecrementBC()
            0x0Cu -> InstructionIncrementC()
            0x0Du -> InstructionDecrementC()
            0x11u -> InstructionLoadDEd16()
            0x13u -> InstructionIncrementDE()
            0x14u -> InstructionIncrementD()
            0x15u -> InstructionDecrementD()
            0x16u -> InstructionLoadDd8()
            0x1Bu -> InstructionDecrementDE()
            0x1Cu -> InstructionIncrementE()
            0x1Du -> InstructionDecrementE()
            0x21u -> InstructionLoadHLd16()
            0x23u -> InstructionIncrementHL()
            0x24u -> InstructionIncrementH()
            0x25u -> InstructionDecrementH()
            0x26u -> InstructionLoadHd8()
            0x2Bu -> InstructionDecrementHL()
            0x2Cu -> InstructionIncrementL()
            0x2Du -> InstructionDecrementL()
            0x31u -> InstructionLoadSPd16()
            0x33u -> InstructionIncrementSP()
            0x3Bu -> InstructionDecrementSP()
            0x3Cu -> InstructionIncrementA()
            0x3Du -> InstructionDecrementA()
            else -> {
                throw RuntimeException("8-bit opcode ${opcode.toHexString()} is not yet implemented")
            }
        }
    }

    private fun interpret16BitOpCode(opcode: UByte): Instruction {
        throw RuntimeException("16-bit opcode $BIT16_OPCODE_PREFIX ${opcode.toHexString()} is not yet implemented")
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