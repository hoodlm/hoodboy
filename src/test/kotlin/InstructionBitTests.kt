import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InstructionBitTests: InstructionTestBase() {
    @Test fun testBitTestsAllOnes() {
        for (bitIndex in 0..7) {
            r.A = 0xFFu
            // inverse of expected flags (to make sure they are flipped)
            r.setFlagZ(true)
            r.setFlagN(true)
            r.setFlagH(false)
            InstructionRegisterBitTest(bitIndex) { reg -> reg.A }.invoke(r, m, DATA)
            assertFalse(r.flagZ()) // since register is all ones, this will never be true
            assertFalse(r.flagN())
            assertTrue(r.flagH())
        }
    }

    @Test fun testBitTestsAllZeroes() {
        for (bitIndex in 0..7) {
            r.B = 0x00u
            // inverse of expected flags (to make sure they are flipped)
            r.setFlagZ(true)
            r.setFlagN(true)
            r.setFlagH(false)
            InstructionRegisterBitTest(bitIndex) { reg -> reg.B }.invoke(r, m, DATA)
            assertTrue(r.flagZ()) // since register is all zeros, this will always be true
            assertFalse(r.flagN())
            assertTrue(r.flagH())
        }
    }

    @Test fun testAlternatingZeroOne() {
        r.C = 0b0101_0101u

        mapOf(
            0 to false, 1 to true,
            2 to false, 3 to true,
            4 to false, 5 to true,
            6 to false, 7 to true
        ).forEach { (bitIndex, expectedFlagValue) ->
            InstructionRegisterBitTest(bitIndex) { reg -> reg.C }.invoke(r, m, DATA)
            assertEquals(expectedFlagValue, r.flagZ())
        }
    }

    @Test fun testChunks() {
        r.H = 0b1100_0011u

        mapOf(
            0 to false, 0 to false,
            2 to true, 3 to true,
            4 to true, 5 to true,
            6 to false, 7 to false
        ).forEach { (bitIndex, expectedFlagValue) ->
            InstructionRegisterBitTest(bitIndex) { reg -> reg.H }.invoke(r, m, DATA)
            assertEquals(expectedFlagValue, r.flagZ())
        }
    }
}