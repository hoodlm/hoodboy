import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InstructionCompareTests: InstructionTestBase() {
    @Test fun compareNumberToSelf() {
        val instruction = InstructionCompareToLiteral()
        for (n in 0u..0xFFu) {
            r.A = n.toUByte()
            r.setFlagZ(false)
            r.setFlagN(false)
            r.setFlagC(true)

            instruction.invoke(r, m, listOf(BYTE_0, n.toUByte(), BYTE_2, BYTE_3))
            assertTrue(r.flagZ())
            assertTrue(r.flagN())
            assertFalse(r.flagC())
            assertEquals(r.A, n.toUByte()) // should NOT affect register A

            r.A = 0u
            r.assertZeroedIgnoreFlags()
        }
    }

    @Test fun compareGreaterThan() {
        val instruction = InstructionCompareToLiteral()
        r.A = 0u
        for (n in 1u..0xFFu) {
            r.setFlagZ(true)
            r.setFlagN(false)
            r.setFlagC(false)

            instruction.invoke(r, m, listOf(BYTE_0, n.toUByte(), BYTE_2, BYTE_3))
            assertFalse(r.flagZ())
            assertTrue(r.flagN())
            assertTrue(r.flagC())
            assertEquals(r.A, BYTE_VALUE_ZERO) // should NOT affect register A

            r.assertZeroedIgnoreFlags()
        }
    }

    @Test fun compareLessThan() {
        val instruction = InstructionCompareToLiteral()
        r.A = 0xFFu
        for (n in 0u..0xFEu) {
            r.setFlagZ(true)
            r.setFlagN(false)
            r.setFlagC(true)

            instruction.invoke(r, m, listOf(BYTE_0, n.toUByte(), BYTE_2, BYTE_3))
            assertFalse(r.flagZ())
            assertTrue(r.flagN())
            assertFalse(r.flagC())
            assertEquals(r.A.toUInt(), 0xFFu) // should NOT affect register A
        }
    }

    @Test fun compareHalfCarry() {
        val instruction = InstructionCompareToLiteral()
        r.A = 0xFFu
        for (n in 0u..0x0Fu) {
            r.setFlagH(true)
            instruction.invoke(r, m, listOf(BYTE_0, n.toUByte(), BYTE_2, BYTE_3))
            assertFalse(r.flagH())
        }
        for (n in 0x10u..0x1Fu) {
            r.setFlagH(false)
            instruction.invoke(r, m, listOf(BYTE_0, n.toUByte(), BYTE_2, BYTE_3))
            println("Compare A=0xFF to n=${n.toUByte().toHexString()}")
            assertTrue(r.flagH())
        }
    }

    @Test fun compareToRegisters() {
        // A-to-A is always zero
        for (n in 0u..0xFFu) {
            r.setFlagZ(false)
            r.A = n.toUByte()
            InstructionCompareToA().invoke(r, m, DATA)
            assertTrue(r.flagZ())
        }

        r.B = 2u
        r.C = 3u
        r.D = 4u
        r.E = 5u
        r.H = 6u
        r.L = 7u

        InstructionCompareToB().invoke(r, m, DATA)
        assertFalse(r.flagZ())
        r.A = r.B
        InstructionCompareToB().invoke(r, m, DATA)
        assertTrue(r.flagZ())

        InstructionCompareToC().invoke(r, m, DATA)
        assertFalse(r.flagZ())
        r.A = r.C
        InstructionCompareToC().invoke(r, m, DATA)
        assertTrue(r.flagZ())

        InstructionCompareToD().invoke(r, m, DATA)
        assertFalse(r.flagZ())
        r.A = r.D
        InstructionCompareToD().invoke(r, m, DATA)
        assertTrue(r.flagZ())

        InstructionCompareToE().invoke(r, m, DATA)
        assertFalse(r.flagZ())
        r.A = r.E
        InstructionCompareToE().invoke(r, m, DATA)
        assertTrue(r.flagZ())

        InstructionCompareToH().invoke(r, m, DATA)
        assertFalse(r.flagZ())
        r.A = r.H
        InstructionCompareToH().invoke(r, m, DATA)
        assertTrue(r.flagZ())

        InstructionCompareToL().invoke(r, m, DATA)
        assertFalse(r.flagZ())
        r.A = r.L
        InstructionCompareToL().invoke(r, m, DATA)
        assertTrue(r.flagZ())
    }
}