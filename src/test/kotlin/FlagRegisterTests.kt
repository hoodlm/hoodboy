import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FlagRegisterTests {
    val r = Registers()

    @BeforeEach
    fun reset() {
        r.clear()
    }

    @Test fun testAllZeroAllOne() {
        r.F = 0x00u
        assertFalse(r.flagZ())
        assertFalse(r.flagN())
        assertFalse(r.flagH())
        assertFalse(r.flagC())
        r.F = 0xFFu
        assertTrue(r.flagZ())
        assertTrue(r.flagN())
        assertTrue(r.flagH())
        assertTrue(r.flagC())
    }

    @Test fun testFlagZ() {
        // Exhaustively testing
        assertFalse(r.flagZ())
        for (x in 0b0000_0000u..0b0111_1111u) {
            r.F = x.toUByte()
            assertFalse(r.flagZ(), "should be FALSE for $x")
        }
        for (x in 0b1000_0000u..0b1111_1111u) {
            r.F = x.toUByte()
            assertTrue(r.flagZ(), "should be TRUE for $x")
        }
    }

    @Test fun testFlagN() {
        // Exhaustively testing
        assertFalse(r.flagZ())
        for (x in 0b0000_0000u..0b0011_1111u) {
            println("phase 1 $x")
            r.F = x.toUByte()
            assertFalse(r.flagN(), "should be FALSE for $x")
        }
        for (x in 0b0100_0000u..0b0111_1111u) {
            println("phase 2 $x")
            r.F = x.toUByte()
            assertTrue(r.flagN(), "should be TRUE for $x")
        }
        for (x in 0b1000_0000u..0b1011_1111u) {
            println("phase 3 $x")
            r.F = x.toUByte()
            assertFalse(r.flagN(), "should be FALSE for $x")
        }
        for (x in 0b1100_0000u..0b1111_1111u) {
            println("phase 4 $x")
            r.F = x.toUByte()
            assertTrue(r.flagN(), "should be TRUE for $x")
        }
    }

    @Test fun testFlagH() {
        // Not exhaustively testing H
        assertFalse(r.flagH())
        for (x in 0b0000_0000u..0b0001_1111u) {
            println("phase 1 $x")
            r.F = x.toUByte()
            assertFalse(r.flagH(), "should be FALSE for $x")
        }
        for (x in 0b0010_0000u..0b0011_1111u) {
            println("phase 2 $x")
            r.F = x.toUByte()
            assertTrue(r.flagH(), "should be TRUE for $x")
        }
        for (x in 0b0100_0000u..0b0101_1111u) {
            println("phase 3 $x")
            r.F = x.toUByte()
            assertFalse(r.flagH(), "should be FALSE for $x")
        }
    }

    @Test fun testFlagC() {
        // Not exhaustively testing C
        assertFalse(r.flagH())
        for (x in 0b0000_0000u..0b0000_1111u) {
            println("phase 1 $x")
            r.F = x.toUByte()
            assertFalse(r.flagC(), "should be FALSE for $x")
        }
        for (x in 0b1110_0000u..0b1110_1111u) {
            println("phase 2 $x")
            r.F = x.toUByte()
            assertTrue(r.flagC(), "should be FALSE for $x")
        }
        for (x in 0b0101_0000u..0b0101_1111u) {
            println("phase 3 $x")
            r.F = x.toUByte()
            assertTrue(r.flagC(), "should be TRUE for $x")
        }
    }
}