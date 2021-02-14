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

    @Test fun testIncrementDecrementSingleByteRegisters() {
        r.B = 12u
        r.D = 22u
        r.H = 32u
        InstructionIncrementB().invoke(r, m, DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(22, r.D.toInt())
        assertEquals(32, r.H.toInt())
        InstructionIncrementB().invoke(r, m, DATA)
        assertEquals(14, r.B.toInt())
        assertEquals(22, r.D.toInt())
        assertEquals(32, r.H.toInt())
        InstructionDecrementB().invoke(r, m, DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(22, r.D.toInt())
        assertEquals(32, r.H.toInt())

        InstructionIncrementD().invoke(r, m, DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(23, r.D.toInt())
        assertEquals(32, r.H.toInt())
        InstructionIncrementD().invoke(r, m, DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(24, r.D.toInt())
        assertEquals(32, r.H.toInt())
        InstructionDecrementD().invoke(r, m, DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(23, r.D.toInt())
        assertEquals(32, r.H.toInt())

        InstructionIncrementH().invoke(r, m, DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(23, r.D.toInt())
        assertEquals(33, r.H.toInt())
        InstructionIncrementH().invoke(r, m, DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(23, r.D.toInt())
        assertEquals(34, r.H.toInt())
        InstructionDecrementH().invoke(r, m, DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(23, r.D.toInt())
        assertEquals(33, r.H.toInt())

        for (i in 1..13) {
            InstructionDecrementB().invoke(r, m, DATA)
        }
        for (i in 1..23) {
            InstructionDecrementD().invoke(r, m, DATA)
        }
        for (i in 1..33) {
            InstructionDecrementH().invoke(r, m, DATA)
        }
        assertTrue(r.flagZ()) // last operation zeroed a register
        assertTrue(r.flagN()) // last operation was a subtraction
        assertFalse(r.flagC())
        assertFalse(r.flagH())
        r.setFlagZ(false)
        r.setFlagN(false)
        r.assertZeroed()
    }

    @Test fun testIncrementDecrementSingleByteRegistersLowEffort() {
        val one: UByte = 1u
        val zero: UByte = 0u

        InstructionIncrementA().invoke(r, m, DATA)
        assertEquals(one, r.A)
        InstructionDecrementA().invoke(r, m, DATA)
        assertTrue(r.flagZ())
        r.assertZeroedIgnoreFlags()

        InstructionIncrementB().invoke(r, m, DATA)
        assertEquals(one, r.B)
        InstructionDecrementB().invoke(r, m, DATA)
        r.assertZeroedIgnoreFlags()

        InstructionIncrementC().invoke(r, m, DATA)
        assertEquals(one, r.C)
        InstructionDecrementC().invoke(r, m, DATA)
        r.assertZeroedIgnoreFlags()

        InstructionIncrementD().invoke(r, m, DATA)
        assertEquals(one, r.D)
        InstructionDecrementD().invoke(r, m, DATA)
        r.assertZeroedIgnoreFlags()

        InstructionIncrementE().invoke(r, m, DATA)
        assertEquals(one, r.E)
        InstructionDecrementE().invoke(r, m, DATA)
        r.assertZeroedIgnoreFlags()

        InstructionIncrementF().invoke(r, m, DATA)
        assertEquals(one, r.F)
        InstructionDecrementF().invoke(r, m, DATA)
        r.assertZeroedIgnoreFlags()

        InstructionIncrementH().invoke(r, m, DATA)
        assertEquals(one, r.H)
        InstructionDecrementH().invoke(r, m, DATA)
        r.assertZeroedIgnoreFlags()

        InstructionIncrementL().invoke(r, m, DATA)
        assertEquals(one, r.L)
        InstructionDecrementL().invoke(r, m, DATA)
        r.assertZeroedIgnoreFlags()
    }

    @Test fun testIncrementSingleByteRegistersFlags() {
        // H (Half-carry flag)
        r.A = 0b00001111u
        InstructionIncrementA().invoke(r, m, DATA)
        assertTrue(r.flagH())
        r.A = 0b01101111u
        InstructionIncrementA().invoke(r, m, DATA)
        assertTrue(r.flagH())
        InstructionIncrementA().invoke(r, m, DATA)
        assertFalse(r.flagH())

        // Z is only set on overflow
        // C is never touched
        // N is always SET to false
        for (x in 0u..0b1111_1110u) {
            r.setFlagZ(true)
            r.A = x.toUByte()
            InstructionIncrementA().invoke(r, m, DATA)
            assertFalse(r.flagC())
            assertFalse(r.flagZ())
            assertFalse(r.flagN())
        }
        // incrementing 0xFF and overflowing is the only way Z is set:
        r.A = 0xFFu
        InstructionIncrementA().invoke(r, m, DATA)
        assertTrue(r.flagZ())
        assertEquals(0u, r.A.toUInt())
    }

    @Test fun testDecrementSingleByteRegistersFlags() {
        // H (Half-carry flag)
        r.A = 0b0000_1111u
        InstructionDecrementA().invoke(r, m, DATA)
        assertFalse(r.flagH())
        r.A = 0b0001_0000u
        InstructionDecrementA().invoke(r, m, DATA)
        assertEquals(0b0000_1111u, r.A.toUInt())
        assertTrue(r.flagH())

        // Z is only set on result of zero
        // C is never touched
        // N is always SET to true
        for (x in 2u..0xFFu) {
            r.setFlagZ(true)
            r.A = x.toUByte()
            InstructionDecrementA().invoke(r, m, DATA)
            assertFalse(r.flagC())
            assertFalse(r.flagZ())
            assertTrue(r.flagN())
        }
        // decrementing from zero (underflow) does not set Z, but does count as a CARRY
        r.A = 0u
        InstructionDecrementA().invoke(r, m, DATA)
        assertEquals(UByte.MAX_VALUE, r.A)
        assertFalse(r.flagZ())
        assertTrue(r.flagH())

        r.A = 1u
        InstructionDecrementA().invoke(r, m, DATA)
        assertTrue(r.flagZ())
    }

    @Test fun testDecrementHalfCarryFlag() {
        for (n in 0b0000_0001u..0b0000_1111u) {
            r.A = n.toUByte()
            InstructionDecrementA().invoke(r, m, DATA)
            assertEquals(n.dec().toUByte(), r.A)
            assertFalse(r.flagH())
        }

        for (n in 0b1111_0001u..0b1111_1111u) {
            r.A = n.toUByte()
            InstructionDecrementA().invoke(r, m, DATA)
            assertEquals(n.dec().toUByte(), r.A)
            assertFalse(r.flagH())
        }

        r.A = 0b0001_0000u
        InstructionDecrementA().invoke(r, m, DATA)
        assertEquals(0b0000_1111u.toUByte(), r.A)
        assertTrue(r.flagH())
    }

    @Test fun testIncrementDecrementDoubleRegisters() {
        val zero: UShort = 0u
        val max: UShort = 0xFEDCu
        // count up for each of BC, DE, HL, SP
        for (i in 1..max.toInt()) {
            assertEquals((i - 1).toUShort(), r.BC())
            InstructionIncrementBC().invoke(r, m, DATA)
            assertEquals(i.toUShort(), r.BC())
            assertEquals(zero, r.DE())
            assertEquals(zero, r.HL())
            assertEquals(zero, r.SP())
            assertFalse(r.flagZ())
            assertFalse(r.flagN())
        }
        for (i in 1..max.toInt()) {
            assertEquals((i - 1).toUShort(), r.DE())
            InstructionIncrementDE().invoke(r, m, DATA)
            assertEquals(i.toUShort(), r.DE())
            assertEquals(max, r.BC())
            assertEquals(zero, r.HL())
            assertEquals(zero, r.SP())
            assertFalse(r.flagZ())
            assertFalse(r.flagN())
        }
        for (i in 1..max.toInt()) {
            assertEquals((i - 1).toUShort(), r.HL())
            InstructionIncrementHL().invoke(r, m, DATA)
            assertEquals(i.toUShort(), r.HL())
            assertEquals(max, r.BC())
            assertEquals(max, r.DE())
            assertEquals(zero, r.SP())
            assertFalse(r.flagZ())
            assertFalse(r.flagN())
        }
        for (i in 1..max.toInt()) {
            assertEquals((i - 1).toUShort(), r.SP())
            InstructionIncrementSP().invoke(r, m, DATA)
            assertEquals(i.toUShort(), r.SP())
            assertEquals(max, r.BC())
            assertEquals(max, r.DE())
            assertEquals(max, r.HL())
            assertFalse(r.flagZ())
            assertFalse(r.flagN())
        }
        // count down for each of BC, DE, HL, SP
        for (i in 1..max.toInt()) {
            assertEquals((max.toInt() - i + 1).toUShort(), r.BC())
            InstructionDecrementBC().invoke(r, m, DATA)
            assertEquals((max.toInt() - i).toUShort(), r.BC())
            assertEquals(max, r.DE())
            assertEquals(max, r.HL())
            assertEquals(max, r.SP())
            assertFalse(r.flagZ())
            assertTrue(r.flagN())
        }
        for (i in 1..max.toInt()) {
            assertEquals((max.toInt() - i + 1).toUShort(), r.DE())
            InstructionDecrementDE().invoke(r, m, DATA)
            assertEquals((max.toInt() - i).toUShort(), r.DE())
            assertEquals(zero, r.BC())
            assertEquals(max, r.HL())
            assertEquals(max, r.SP())
            assertFalse(r.flagZ())
            assertTrue(r.flagN())
        }
        for (i in 1..max.toInt()) {
            assertEquals((max.toInt() - i + 1).toUShort(), r.HL())
            InstructionDecrementHL().invoke(r, m, DATA)
            assertEquals((max.toInt() - i).toUShort(), r.HL())
            assertEquals(zero, r.BC())
            assertEquals(zero, r.DE())
            assertEquals(max, r.SP())
            assertFalse(r.flagZ())
            assertTrue(r.flagN())
        }
        for (i in 1..max.toInt()) {
            assertEquals((max.toInt() - i + 1).toUShort(), r.SP())
            InstructionDecrementSP().invoke(r, m, DATA)
            assertEquals((max.toInt() - i).toUShort(), r.SP())
            assertEquals(zero, r.BC())
            assertEquals(zero, r.DE())
            assertEquals(zero, r.HL())
            assertFalse(r.flagZ())
            assertTrue(r.flagN())
        }
        // finally make sure everything is zero
        r.setFlagN(false)
        r.setFlagZ(false)
        r.assertZeroed()
    }
}