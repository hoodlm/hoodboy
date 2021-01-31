import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InstructionLoadFromRegisterToMemoryTests: InstructionTestBase() {

    @Test
    fun testHL_Decrement() {
        val instruction: Instruction = InstructionLoadAFromHLDecrement()
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
        val instruction: Instruction = InstructionLoadAFromHLIncrement()
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
}