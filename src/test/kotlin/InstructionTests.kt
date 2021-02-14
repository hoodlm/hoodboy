import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.AssertionError

open class InstructionTests: InstructionTestBase() {
    @Test fun testNOOP() {
        val instruction = InstructionNOOP()
        instruction.invoke(r, m, DATA)
        r.assertZeroed()
    }

    @Test fun testLoadBC16dNegative() {
        val instruction = InstructionLoadBCd16()
        listOf(NO_DATA, ONE_BYTE_DATA).forEach {
            assertThrows(AssertionError::class.java) {
                instruction.invoke(r, m, it)
            }
        }
    }

    @Test fun testLoadBd8Negative() {
        val instruction = InstructionLoadBd8()
        assertThrows(AssertionError::class.java) {
            instruction.invoke(r, m, NO_DATA)
        }
    }

    @Test fun testLoadBd8Happy() {
        val instruction = InstructionLoadBd8()
        instruction.invoke(r, m, DATA)
        assertEquals(r.B, BYTE_1)
        // if we reset this register, all registers should be back to zero
        r.B = 0u
        r.assertZeroed()
    }

    @Test fun testLoadDd8Happy() {
        val instruction = InstructionLoadDd8()
        instruction.invoke(r, m, DATA)
        assertEquals(r.D, BYTE_1)
        // if we reset this register, all registers should be back to zero
        r.D = 0u
        r.assertZeroed()
    }

    @Test fun testLoadHd8Happy() {
        val instruction = InstructionLoadHd8()
        instruction.invoke(r, m, DATA)
        assertEquals(r.H, BYTE_1)
        // if we reset this register, all registers should be back to zero
        r.H = 0u
        r.assertZeroed()
    }

    @Test fun testLoadBC16dHappy() {
        val instruction = InstructionLoadBCd16()
        instruction.invoke(r, m, DATA)
        assertEquals(r.B, BYTE_1)
        assertEquals(r.C, BYTE_2)
        // if we reset those two registers, all registers should be back to zero
        r.B = 0u
        r.C = 0u
        r.assertZeroed()
    }

    @Test fun testLoadDE16dHappy() {
        val instruction = InstructionLoadDEd16()
        instruction.invoke(r, m, DATA)
        assertEquals(r.D, BYTE_1)
        assertEquals(r.E, BYTE_2)
        // if we reset those two registers, all registers should be back to zero
        r.D = 0u
        r.E = 0u
        r.assertZeroed()
    }

    @Test fun testLoadHL16dHappy() {
        val instruction = InstructionLoadHLd16()
        instruction.invoke(r, m, DATA)
        assertEquals(r.H, BYTE_1)
        assertEquals(r.L, BYTE_2)
        // if we reset those two registers, all registers should be back to zero
        r.H = 0u
        r.L = 0u
        r.assertZeroed()
    }

    @Test fun testLoadSP16dHappy() {
        val instruction = InstructionLoadSPd16()
        instruction.invoke(r, m, DATA)
        val expectedShort: UShort = Pair(DATA[1], DATA[2]).toUShort()
        assertEquals(r.SP(), expectedShort)
        // if we reset this register, all registers should be back to zero
        r.setSP(0u)
        r.assertZeroed()
    }
}