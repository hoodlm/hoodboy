import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InstructionRotateTests: InstructionTestBase() {
    @Test
    fun testRotateLeftNoCarryBitSimple() {
        r.B = 0b0000_0011u
        val rotateLeft = {
            r.setFlagC(false)
            InstructionRotateLeftB().invoke(r, m, DATA)
        }
        rotateLeft()
        assertEquals(0b0000_0110u, r.B.toUInt())
        rotateLeft()
        assertEquals(0b0000_1100u, r.B.toUInt())
        rotateLeft()
        assertEquals(0b0001_1000u, r.B.toUInt())
        rotateLeft()
        assertEquals(0b0011_0000u, r.B.toUInt())
        rotateLeft()
        assertEquals(0b0110_0000u, r.B.toUInt())
        rotateLeft()
        assertEquals(0b1100_0000u, r.B.toUInt())
        rotateLeft()
        assertEquals(0b1000_0000u, r.B.toUInt())
        rotateLeft()
        assertEquals(0b0000_0000u, r.B.toUInt())
        rotateLeft()
        assertEquals(0b0000_0000u, r.B.toUInt())
    }

    @Test
    fun testRotateLeftWithCarryBit() {
        r.B = 0b0000_0011u
        val rotateLeftWithCarryFlagTrue = {
            r.setFlagC(true)
            InstructionRotateLeftB().invoke(r, m, DATA)
        }
        val rotateLeftWithCarryFlagFalse = {
            r.setFlagC(false)
            InstructionRotateLeftB().invoke(r, m, DATA)
        }
        rotateLeftWithCarryFlagTrue()
        assertEquals(0b0000_0111u, r.B.toUInt())
        rotateLeftWithCarryFlagTrue()
        assertEquals(0b0000_1111u, r.B.toUInt())
        rotateLeftWithCarryFlagTrue()
        assertEquals(0b0001_1111u, r.B.toUInt())
        rotateLeftWithCarryFlagFalse()
        assertEquals(0b0011_1110u, r.B.toUInt())
        rotateLeftWithCarryFlagFalse()
        assertEquals(0b0111_1100u, r.B.toUInt())
        rotateLeftWithCarryFlagTrue()
        assertEquals(0b1111_1001u, r.B.toUInt())
        rotateLeftWithCarryFlagTrue()
        assertEquals(0b1111_0011u, r.B.toUInt())
        rotateLeftWithCarryFlagTrue()
        assertEquals(0b1110_0111u, r.B.toUInt())
        rotateLeftWithCarryFlagTrue()
        assertEquals(0b1100_1111u, r.B.toUInt())
        rotateLeftWithCarryFlagTrue()
        assertEquals(0b1001_1111u, r.B.toUInt())
        rotateLeftWithCarryFlagTrue()
        assertEquals(0b0011_1111u, r.B.toUInt())
        rotateLeftWithCarryFlagTrue()
        assertEquals(0b0111_1111u, r.B.toUInt())
        rotateLeftWithCarryFlagTrue()
        assertEquals(0b1111_1111u, r.B.toUInt())
        rotateLeftWithCarryFlagTrue()
        assertEquals(0b1111_1111u, r.B.toUInt())
    }

    @Test
    fun testRotateRightWithCarryBit() {
        r.B = 0b0001_0000u
        val rotateRightWithCarryFlagTrue = {
            r.setFlagC(true)
            InstructionRotateRightB().invoke(r, m, DATA)
        }
        val rotateRightWithCarryFlagFalse = {
            r.setFlagC(false)
            InstructionRotateRightB().invoke(r, m, DATA)
        }
        rotateRightWithCarryFlagTrue()
        assertEquals(0b1000_1000u, r.B.toUInt())
        rotateRightWithCarryFlagTrue()
        assertEquals(0b1100_0100u, r.B.toUInt())
        rotateRightWithCarryFlagTrue()
        assertEquals(0b1110_0010u, r.B.toUInt())
        rotateRightWithCarryFlagFalse()
        assertEquals(0b0111_0001u, r.B.toUInt())
    }

    @Test
    fun testRotateLeftSetsZeroFlag() {
        r.B = 0b1000_0000u
        InstructionRotateLeftB().invoke(r, m, DATA)
        assertEquals(BYTE_VALUE_ZERO, r.B)
        assertTrue(r.flagZ())

        r.setFlagC(false)
        InstructionRotateLeftB().invoke(r, m, DATA)
        assertEquals(BYTE_VALUE_ZERO, r.B)
        assertTrue(r.flagZ())

        // Anything non-zero should set this back to false
        r.B = 0b1111_1111u
        InstructionRotateLeftB().invoke(r, m, DATA)
        assertFalse(r.flagZ())
    }

    @Test
    fun testRotateRightSetsZeroFlag() {
        r.B = 0b0000_0001u
        InstructionRotateRightB().invoke(r, m, DATA)
        assertEquals(BYTE_VALUE_ZERO, r.B)
        assertTrue(r.flagZ())

        r.setFlagC(false)
        InstructionRotateRightB().invoke(r, m, DATA)
        assertEquals(BYTE_VALUE_ZERO, r.B)
        assertTrue(r.flagZ())

        // Anything non-zero should set this back to false
        r.B = 0b1111_1111u
        InstructionRotateRightB().invoke(r, m, DATA)
        assertFalse(r.flagZ())
    }

    @Test
    fun testRotateRightANoZeroFlag() {
        val instruction = InstructionRotateRightANoZeroFlag()
        for (i in 0x00u..0xFFu) {
            r.setFlagN(true)
            r.setFlagH(true)
            r.setFlagZ(true)
            r.B = i.toUByte()
            instruction.invoke(r, m, DATA)
            assertFalse(r.flagN())
            assertFalse(r.flagH())
            assertFalse(r.flagZ())
        }
    }

    @Test
    fun testRotateAlwaysSetsHNFlagsFalse() {
        listOf(InstructionRotateLeftB(), InstructionRotateRightB()).forEach { instruction ->
            for (i in 0x00u..0xFFu) {
                r.setFlagN(true)
                r.setFlagH(true)
                r.B = i.toUByte()
                instruction.invoke(r, m, DATA)
                assertFalse(r.flagN())
                assertFalse(r.flagH())
            }
        }
    }

    @Test
    fun testRotateLeftSetsCarryFlag() {
        val instruction = InstructionRotateLeftB()
        r.B = 0b1000_0000u
        instruction.invoke(r, m, DATA)
        assertEquals(BYTE_VALUE_ZERO, r.B)
        assertTrue(r.flagC())

        r.setFlagC(false)
        r.B = 0b1010_1010u
        instruction.invoke(r, m, DATA)
        assertTrue(r.flagC())
    }

    @Test
    fun testRotateRightSetsCarryFlag() {
        val instruction = InstructionRotateRightB()
        r.B = 0b0000_0001u
        instruction.invoke(r, m, DATA)
        assertEquals(BYTE_VALUE_ZERO, r.B)
        assertTrue(r.flagC())

        r.setFlagC(false)
        r.B = 0b0101_0101u
        instruction.invoke(r, m, DATA)
        assertTrue(r.flagC())
    }

    @Test
    fun rotateEachRegister() {
        r.A = 0b0100_0000u
        InstructionRotateLeftA().invoke(r, m, DATA)
        assertEquals(0b1000_0000u, r.A.toUInt())
        InstructionRotateRightA().invoke(r, m, DATA)
        assertEquals(0b0100_0000u, r.A.toUInt())

        r.B = 0b0110_0000u
        InstructionRotateLeftB().invoke(r, m, DATA)
        assertEquals(0b1100_0000u, r.B.toUInt())
        InstructionRotateRightB().invoke(r, m, DATA)
        assertEquals(0b0110_0000u, r.B.toUInt())

        r.C = 0b0111_0000u
        InstructionRotateLeftC().invoke(r, m, DATA)
        assertEquals(0b1110_0000u, r.C.toUInt())
        InstructionRotateRightC().invoke(r, m, DATA)
        assertEquals(0b0111_0000u, r.C.toUInt())

        r.D = 0b0110_1000u
        InstructionRotateLeftD().invoke(r, m, DATA)
        assertEquals(0b1101_0000u, r.D.toUInt())
        InstructionRotateRightD().invoke(r, m, DATA)
        assertEquals(0b0110_1000u, r.D.toUInt())

        r.E = 0b0110_1100u
        InstructionRotateLeftE().invoke(r, m, DATA)
        assertEquals(0b1101_1000u, r.E.toUInt())
        InstructionRotateRightE().invoke(r, m, DATA)
        assertEquals(0b0110_1100u, r.E.toUInt())

        r.H = 0b0110_1111u
        InstructionRotateLeftH().invoke(r, m, DATA)
        assertEquals(0b1101_1110u, r.H.toUInt())
        InstructionRotateRightH().invoke(r, m, DATA)
        assertEquals(0b0110_1111u, r.H.toUInt())

        r.L = 0b0000_0011u
        InstructionRotateLeftL().invoke(r, m, DATA)
        assertEquals(0b0000_0110u, r.L.toUInt())
        InstructionRotateRightL().invoke(r, m, DATA)
        assertEquals(0b0000_0011u, r.L.toUInt())
    }
}