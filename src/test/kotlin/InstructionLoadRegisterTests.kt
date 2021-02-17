import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.AssertionError

open class InstructionLoadRegisterToRegisterTests: InstructionTestBase() {
    @Test fun loadSelf() {
        r.B = BYTE_3
        val instruction = InstructionLoadRegisterToRegister({ it::B }, { it::B })
        instruction.invoke(r, m, DATA)
        assertEquals(BYTE_3, r.B)
        r.B = BYTE_VALUE_ZERO
        r.assertZeroed()
    }

    @Test fun loadAcrossRegister() {
        r.B = BYTE_2
        val instruction = InstructionLoadRegisterToRegister({ it::B }, { it::C })
        instruction.invoke(r, m, DATA)
        assertEquals(BYTE_2, r.B)
        assertEquals(BYTE_2, r.C)
        r.B = BYTE_VALUE_ZERO
        r.C = BYTE_VALUE_ZERO
        r.assertZeroed()
    }
}