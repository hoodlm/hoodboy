import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InstructionJumpTests: InstructionTestBase() {
    private val JUMP_DATA_16: List<UByte> = listOf(
        0x00u, 0x10u, 0x0u, 0x0u)
    private val JUMP_DATA_8: List<UByte> = listOf(
        0x00u, 0x08u, 0x0u, 0x0u)

    @Test
    fun testInstructionJumpUnconditional() {
        r.setPC(0x00u)
        InstructionJumpUnconditional().invoke(r, m, JUMP_DATA_16)
        assertEquals(0x10u, r.PC().toUInt())

        InstructionJumpUnconditional().invoke(r, m, JUMP_DATA_8)
        assertEquals(0x18u, r.PC().toUInt())

        r.setPC(0x0u)
        r.assertZeroed()
    }

    @Test
    fun testInstructionJumpConditionals() {
        r.setPC(0x00u)
        r.setFlagZ(true)
        InstructionJumpNotZ().invoke(r, m, JUMP_DATA_16)
        r.assertZeroedIgnoreFlags()
        r.setFlagC(true)
        InstructionJumpNotC().invoke(r, m, JUMP_DATA_16)
        r.assertZeroedIgnoreFlags()
        InstructionJumpZ().invoke(r, m, JUMP_DATA_16)
        assertEquals(0x10u, r.PC().toUInt())

        r.setFlagZ(false)
        InstructionJumpC().invoke(r, m, JUMP_DATA_16)
        assertEquals(0x20u, r.PC().toUInt())

        r.setPC(0u)
        r.setFlagC(false)
        InstructionJumpZ().invoke(r, m, JUMP_DATA_16)
        r.assertZeroed()
        InstructionJumpC().invoke(r, m, JUMP_DATA_16)
        r.assertZeroed()

        InstructionJumpNotZ().invoke(r, m, JUMP_DATA_16)
        assertEquals(0x10u, r.PC().toUInt())
        InstructionJumpNotC().invoke(r, m, JUMP_DATA_16)
        assertEquals(0x20u, r.PC().toUInt())
        r.setPC(0u)
        r.assertZeroed()
    }
}