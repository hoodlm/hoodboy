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
            0x0Eu -> InstructionLoadCd8()
            0x11u -> InstructionLoadDEd16()
            0x13u -> InstructionIncrementDE()
            0x14u -> InstructionIncrementD()
            0x15u -> InstructionDecrementD()
            0x16u -> InstructionLoadDd8()
            0x18u -> InstructionJumpUnconditional()
            0x1Bu -> InstructionDecrementDE()
            0x1Cu -> InstructionIncrementE()
            0x1Du -> InstructionDecrementE()
            0x1Eu -> InstructionLoadEd8()
            0x20u -> InstructionJumpNotZ()
            0x21u -> InstructionLoadHLd16()
            0x22u -> InstructionLoadAFromHLIncrement()
            0x23u -> InstructionIncrementHL()
            0x24u -> InstructionIncrementH()
            0x25u -> InstructionDecrementH()
            0x26u -> InstructionLoadHd8()
            0x28u -> InstructionJumpZ()
            0x2Bu -> InstructionDecrementHL()
            0x2Cu -> InstructionIncrementL()
            0x2Du -> InstructionDecrementL()
            0x2Eu -> InstructionLoadLd8()
            0x30u -> InstructionJumpNotC()
            0x31u -> InstructionLoadSPd16()
            0x32u -> InstructionLoadAFromHLDecrement()
            0x33u -> InstructionIncrementSP()
            0x38u -> InstructionJumpC()
            0x3Bu -> InstructionDecrementSP()
            0x3Cu -> InstructionIncrementA()
            0x3Du -> InstructionDecrementA()
            0x3Eu -> InstructionLoadAd8()
            0x77u -> InstructionLoadAFromHL()
            0xAFu -> InstructionXorA()
            0xE2u -> InstructionLoadAFromC()
            else -> {
                throw RuntimeException("8-bit opcode ${opcode.toHexString()} is not yet implemented")
            }
        }
    }

    private fun interpret16BitOpCode(opcode: UByte): Instruction {
        return when (opcode.toUInt()) {
            0x40u -> InstructionRegisterBitTest(0) { r -> r.B }
            0x41u -> InstructionRegisterBitTest(0) { r -> r.C }
            0x42u -> InstructionRegisterBitTest(0) { r -> r.D }
            0x43u -> InstructionRegisterBitTest(0) { r -> r.E }
            0x44u -> InstructionRegisterBitTest(0) { r -> r.H }
            0x45u -> InstructionRegisterBitTest(0) { r -> r.L }
            // 0x46
            0x47u -> InstructionRegisterBitTest(0) { r -> r.A }
            0x48u -> InstructionRegisterBitTest(1) { r -> r.B }
            0x49u -> InstructionRegisterBitTest(1) { r -> r.C }
            0x4Au -> InstructionRegisterBitTest(1) { r -> r.D }
            0x4Bu -> InstructionRegisterBitTest(1) { r -> r.E }
            0x4Cu -> InstructionRegisterBitTest(1) { r -> r.H }
            0x4Du -> InstructionRegisterBitTest(1) { r -> r.L }
            // 0x4E
            0x4Fu -> InstructionRegisterBitTest(1) { r -> r.A }
            0x50u -> InstructionRegisterBitTest(2) { r -> r.B }
            0x51u -> InstructionRegisterBitTest(2) { r -> r.C }
            0x52u -> InstructionRegisterBitTest(2) { r -> r.D }
            0x53u -> InstructionRegisterBitTest(2) { r -> r.E }
            0x54u -> InstructionRegisterBitTest(2) { r -> r.H }
            0x55u -> InstructionRegisterBitTest(2) { r -> r.L }
            // 0x56
            0x57u -> InstructionRegisterBitTest(2) { r -> r.A }
            0x58u -> InstructionRegisterBitTest(3) { r -> r.B }
            0x59u -> InstructionRegisterBitTest(3) { r -> r.C }
            0x5Au -> InstructionRegisterBitTest(3) { r -> r.D }
            0x5Bu -> InstructionRegisterBitTest(3) { r -> r.E }
            0x5Cu -> InstructionRegisterBitTest(3) { r -> r.H }
            0x5Du -> InstructionRegisterBitTest(3) { r -> r.L }
            // 0x5E
            0x5Fu -> InstructionRegisterBitTest(3) { r -> r.A }
            0x60u -> InstructionRegisterBitTest(4) { r -> r.B }
            0x61u -> InstructionRegisterBitTest(4) { r -> r.C }
            0x62u -> InstructionRegisterBitTest(4) { r -> r.D }
            0x63u -> InstructionRegisterBitTest(4) { r -> r.E }
            0x64u -> InstructionRegisterBitTest(4) { r -> r.H }
            0x65u -> InstructionRegisterBitTest(4) { r -> r.L }
            // 0x66
            0x67u -> InstructionRegisterBitTest(4) { r -> r.A }
            0x68u -> InstructionRegisterBitTest(5) { r -> r.B }
            0x69u -> InstructionRegisterBitTest(5) { r -> r.C }
            0x6Au -> InstructionRegisterBitTest(5) { r -> r.D }
            0x6Bu -> InstructionRegisterBitTest(5) { r -> r.E }
            0x6Cu -> InstructionRegisterBitTest(5) { r -> r.H }
            0x6Du -> InstructionRegisterBitTest(5) { r -> r.L }
            // 0x6E
            0x6Fu -> InstructionRegisterBitTest(5) { r -> r.A }
            0x70u -> InstructionRegisterBitTest(6) { r -> r.B }
            0x71u -> InstructionRegisterBitTest(6) { r -> r.C }
            0x72u -> InstructionRegisterBitTest(6) { r -> r.D }
            0x73u -> InstructionRegisterBitTest(6) { r -> r.E }
            0x74u -> InstructionRegisterBitTest(6) { r -> r.H }
            0x75u -> InstructionRegisterBitTest(6) { r -> r.L }
            // 0x76
            0x77u -> InstructionRegisterBitTest(6) { r -> r.A }
            0x78u -> InstructionRegisterBitTest(7) { r -> r.B }
            0x79u -> InstructionRegisterBitTest(7) { r -> r.C }
            0x7Au -> InstructionRegisterBitTest(7) { r -> r.D }
            0x7Bu -> InstructionRegisterBitTest(7) { r -> r.E }
            0x7Cu -> InstructionRegisterBitTest(7) { r -> r.H }
            0x7Du -> InstructionRegisterBitTest(7) { r -> r.L }
            // 0x7E
            0x7Fu -> InstructionRegisterBitTest(7) { r -> r.A }
            else -> {
                val hexString = listOf(BIT16_OPCODE_PREFIX, opcode).toHexString()
                throw RuntimeException("16-bit opcode $hexString is not yet implemented")
            }
        }
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