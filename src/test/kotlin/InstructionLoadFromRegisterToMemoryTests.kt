import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InstructionLoadFromRegisterToMemoryTests: InstructionTestBase() {

    @Test
    fun testLoadToAddressInHL() {
        val address: UShort = 0x8765u
        r.setHL(address)
        r.A = BYTE_3
        InstructionLoadFromAToHL().invoke(r, m, DATA)
        assertEquals(BYTE_3, m.getByte(address))
        // A and HL should be unaffected!
        assertEquals(address, r.HL())
        assertEquals(BYTE_3, r.A)
        r.A = 0u

        r.B = BYTE_2
        InstructionLoadFromBToHL().invoke(r, m, DATA)
        assertEquals(BYTE_2, m.getByte(address))
        r.B = 0u

        r.C = BYTE_1
        InstructionLoadFromCToHL().invoke(r, m, DATA)
        assertEquals(BYTE_1, m.getByte(address))
        r.C = 0u

        r.D = BYTE_3
        InstructionLoadFromDToHL().invoke(r, m, DATA)
        assertEquals(BYTE_3, m.getByte(address))
        r.D = 0u

        r.E = BYTE_2
        InstructionLoadFromEToHL().invoke(r, m, DATA)
        assertEquals(BYTE_2, m.getByte(address))
        r.E = 0u

        InstructionLoadFromHToHL().invoke(r, m, DATA)
        assertEquals(0x87u, m.getByte(address).toUInt())

        InstructionLoadFromLToHL().invoke(r, m, DATA)
        assertEquals(0x65u, m.getByte(address).toUInt())

        r.setHL(0u)
        r.assertZeroed()
    }

    @Test
    fun testHL_Decrement() {
        val instruction: Instruction = InstructionLoadFromAToHLDecrement()
        r.setHL(8000u)
        r.A = BYTE_3
        instruction.invoke(r, m, DATA)
        assertEquals(BYTE_3, m.getByte(8000u))
        assertEquals(7999u, r.HL().toUInt())
        // Subsequent calls can count down:
        instruction.invoke(r, m, DATA)
        assertEquals(BYTE_3, m.getByte(7999u))
        assertEquals(7998u, r.HL().toUInt())

        // A and HL should be the only registers affected
        r.A = 0u
        r.setHL(0u)
        r.assertZeroed()
    }

    @Test
    fun testHL_Increment() {
        val instruction: Instruction = InstructionLoadFromAToHLIncrement()
        r.setHL(8100u)
        r.A = BYTE_2
        instruction.invoke(r, m, DATA)
        assertEquals(BYTE_2, m.getByte(8100u))
        assertEquals(8101u, r.HL().toUInt())
        // Subsequent calls can count down:
        instruction.invoke(r, m, DATA)
        assertEquals(BYTE_2, m.getByte(8101u))
        assertEquals(8102u, r.HL().toUInt())

        // A and HL should be the only registers affected
        r.A = 0u
        r.setHL(0u)
        r.assertZeroed()
    }

    @Test
    fun testInstructionLoadAFromC() {
        val instruction: Instruction = InstructionLoadAFromC()
        r.A = BYTE_2
        for (i in 0x00u..0xFFu) {
            r.C = i.toUByte()
            val expectedDestAddress: UShort = (0xFF00u + i).toUShort()
            assertEquals(BYTE_VALUE_ZERO, m.getByte(expectedDestAddress))
            instruction.invoke(r, m, DATA)
            assertEquals(BYTE_2, m.getByte(expectedDestAddress))
        }

        // verify no other registers/flags were affected
        r.A = 0u
        r.C = 0u
        r.assertZeroed()
    }

    @Test
    fun testInstructionLoadAFromLiteral() {
        val instruction: Instruction = InstructionLoadAFrom8BitLiteral()
        r.A = BYTE_3
        for (i in 0x00u..0xFFu) {
            val expectedDestAddress: UShort = (0xFF00u + i).toUShort()
            assertEquals(BYTE_VALUE_ZERO, m.getByte(expectedDestAddress))
            instruction.invoke(r, m, listOf(BYTE_0, i.toUByte(), BYTE_2, BYTE_3))
            assertEquals(BYTE_3, m.getByte(expectedDestAddress))
        }

        // verify no other registers/flags were affected
        r.A = 0u
        r.assertZeroed()
    }

    @Test
    fun test_InstructionLoadAFrom16BitLiteral() {
        val instruction = InstructionLoadAFrom16BitLiteral()
        r.A = 0xCDu
        val destAddress = Pair(DATA[2], DATA[1]).toUShort()
        instruction.invoke(r, m, DATA)
        assertEquals(r.A, m.getByte(destAddress))
        r.A = 0u
        r.assertZeroed()
    }
}