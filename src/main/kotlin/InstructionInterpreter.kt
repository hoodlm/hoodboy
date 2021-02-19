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
            0x0Au -> InstructionLoadFromBCAddressToA()
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
            0x1Au -> InstructionLoadFromDEAddressToA()
            0x1Bu -> InstructionDecrementDE()
            0x1Cu -> InstructionIncrementE()
            0x1Du -> InstructionDecrementE()
            0x1Eu -> InstructionLoadEd8()
            0x20u -> InstructionJumpNotZ()
            0x21u -> InstructionLoadHLd16()
            0x22u -> InstructionLoadFromAToHLIncrement()
            0x23u -> InstructionIncrementHL()
            0x24u -> InstructionIncrementH()
            0x25u -> InstructionDecrementH()
            0x26u -> InstructionLoadHd8()
            0x28u -> InstructionJumpZ()
            0x2Au -> InstructionLoadFromHLAddressToAIncrement()
            0x2Bu -> InstructionDecrementHL()
            0x2Cu -> InstructionIncrementL()
            0x2Du -> InstructionDecrementL()
            0x2Eu -> InstructionLoadLd8()
            0x30u -> InstructionJumpNotC()
            0x31u -> InstructionLoadSPd16()
            0x32u -> InstructionLoadFromAToHLDecrement()
            0x33u -> InstructionIncrementSP()
            0x38u -> InstructionJumpC()
            0x3Au -> InstructionLoadFromHLAddressToADecrement()
            0x3Bu -> InstructionDecrementSP()
            0x3Cu -> InstructionIncrementA()
            0x3Du -> InstructionDecrementA()
            0x3Eu -> InstructionLoadAd8()
            0x40u -> InstructionLoadRegisterToRegister({ it::B }, { it::B })
            0x41u -> InstructionLoadRegisterToRegister({ it::C }, { it::B })
            0x42u -> InstructionLoadRegisterToRegister({ it::D }, { it::B })
            0x43u -> InstructionLoadRegisterToRegister({ it::E }, { it::B })
            0x44u -> InstructionLoadRegisterToRegister({ it::H }, { it::B })
            0x45u -> InstructionLoadRegisterToRegister({ it::L }, { it::B })
            // 0x46
            0x47u -> InstructionLoadRegisterToRegister({ it::A }, { it::B })
            0x48u -> InstructionLoadRegisterToRegister({ it::B }, { it::C })
            0x49u -> InstructionLoadRegisterToRegister({ it::C }, { it::C })
            0x4Au -> InstructionLoadRegisterToRegister({ it::D }, { it::C })
            0x4Bu -> InstructionLoadRegisterToRegister({ it::E }, { it::C })
            0x4Cu -> InstructionLoadRegisterToRegister({ it::H }, { it::C })
            0x4Du -> InstructionLoadRegisterToRegister({ it::L }, { it::C })
            // 0x4E
            0x4Fu -> InstructionLoadRegisterToRegister({ it::A }, { it::C })
            0x50u -> InstructionLoadRegisterToRegister({ it::B }, { it::D })
            0x51u -> InstructionLoadRegisterToRegister({ it::C }, { it::D })
            0x52u -> InstructionLoadRegisterToRegister({ it::D }, { it::D })
            0x53u -> InstructionLoadRegisterToRegister({ it::E }, { it::D })
            0x54u -> InstructionLoadRegisterToRegister({ it::H }, { it::D })
            0x55u -> InstructionLoadRegisterToRegister({ it::L }, { it::D })
            // 0x56
            0x57u -> InstructionLoadRegisterToRegister({ it::A }, { it::D })
            0x58u -> InstructionLoadRegisterToRegister({ it::B }, { it::E })
            0x59u -> InstructionLoadRegisterToRegister({ it::C }, { it::E })
            0x5Au -> InstructionLoadRegisterToRegister({ it::D }, { it::E })
            0x5Bu -> InstructionLoadRegisterToRegister({ it::E }, { it::E })
            0x5Cu -> InstructionLoadRegisterToRegister({ it::H }, { it::E })
            0x5Du -> InstructionLoadRegisterToRegister({ it::L }, { it::E })
            // 0x5E
            0x5Fu -> InstructionLoadRegisterToRegister({ it::A }, { it::E })

            0x60u -> InstructionLoadRegisterToRegister({ it::B }, { it::H })
            0x61u -> InstructionLoadRegisterToRegister({ it::C }, { it::H })
            0x62u -> InstructionLoadRegisterToRegister({ it::D }, { it::H })
            0x63u -> InstructionLoadRegisterToRegister({ it::E }, { it::H })
            0x64u -> InstructionLoadRegisterToRegister({ it::H }, { it::H })
            0x65u -> InstructionLoadRegisterToRegister({ it::L }, { it::H })
            // 0x66
            0x67u -> InstructionLoadRegisterToRegister({ it::A }, { it::H })
            0x68u -> InstructionLoadRegisterToRegister({ it::B }, { it::L })
            0x69u -> InstructionLoadRegisterToRegister({ it::C }, { it::L })
            0x6Au -> InstructionLoadRegisterToRegister({ it::D }, { it::L })
            0x6Bu -> InstructionLoadRegisterToRegister({ it::E }, { it::L })
            0x6Cu -> InstructionLoadRegisterToRegister({ it::H }, { it::L })
            0x6Du -> InstructionLoadRegisterToRegister({ it::L }, { it::L })
            // 0x6E
            0x6Fu -> InstructionLoadRegisterToRegister({ it::A }, { it::L })
            0x70u -> InstructionLoadFromBToHL()
            0x71u -> InstructionLoadFromCToHL()
            0x72u -> InstructionLoadFromDToHL()
            0x73u -> InstructionLoadFromEToHL()
            0x74u -> InstructionLoadFromHToHL()
            0x75u -> InstructionLoadFromLToHL()
            // 0x76
            0x77u -> InstructionLoadFromAToHL()
            0x78u -> InstructionLoadRegisterToRegister({ it::B }, { it::A })
            0x79u -> InstructionLoadRegisterToRegister({ it::C }, { it::A })
            0x7Au -> InstructionLoadRegisterToRegister({ it::D }, { it::A })
            0x7Bu -> InstructionLoadRegisterToRegister({ it::E }, { it::A })
            0x7Cu -> InstructionLoadRegisterToRegister({ it::H }, { it::A })
            0x7Du -> InstructionLoadRegisterToRegister({ it::L }, { it::A })
            0x7Eu -> InstructionLoadFromAToHL()
            0x7Fu -> InstructionLoadRegisterToRegister({ it::A }, { it::A })
            0xAFu -> InstructionXorA()
            0xC1u -> InstructionPopBCFromStack()
            0xC5u -> InstructionPushBCToStack()
            0xC9u -> InstructionReturn()
            0xCDu -> InstructionCallLiteralAddress()
            0xD1u -> InstructionPopDEFromStack()
            0xD5u -> InstructionPushDEToStack()
            0xE0u -> InstructionLoadAFromLiteral()
            0xE1u -> InstructionPopHLFromStack()
            0xE2u -> InstructionLoadAFromC()
            0xE5u -> InstructionPushHLToStack()
            0xF1u -> InstructionPopAFFromStack()
            0xF5u -> InstructionPushAFToStack()
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