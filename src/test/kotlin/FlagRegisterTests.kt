import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FlagRegisterTests {
    val r = Registers()

    @BeforeEach
    fun reset() {
        r.clear()
    }

    @Test fun testAllZeroAllOneGet() {
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

    @Test fun testGetFlagZ() {
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

    @Test fun testGetFlagN() {
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

    @Test fun testGetFlagH() {
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

    @Test fun testGetFlagC() {
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
            assertFalse(r.flagC(), "should be FALSE for $x")
        }
        for (x in 0b0101_0000u..0b0101_1111u) {
            println("phase 3 $x")
            r.F = x.toUByte()
            assertTrue(r.flagC(), "should be TRUE for $x")
        }
    }

    @Test fun testSetters() {
        r.setFlagZ(true)
        assertEquals(0b1000_0000u, r.F.toUInt())
        r.setFlagN(true)
        assertEquals(0b1100_0000u, r.F.toUInt())
        r.setFlagH(true)
        assertEquals(0b1110_0000u, r.F.toUInt())
        r.setFlagC(true)
        assertEquals(0b1111_0000u, r.F.toUInt())

        // Should be NOOPs
        r.setFlagZ(true)
        assertEquals(0b1111_0000u, r.F.toUInt())
        r.setFlagN(true)
        assertEquals(0b1111_0000u, r.F.toUInt())
        r.setFlagH(true)
        assertEquals(0b1111_0000u, r.F.toUInt())
        r.setFlagC(true)
        assertEquals(0b1111_0000u, r.F.toUInt())

        r.setFlagH(false)
        assertEquals(0b1101_0000u, r.F.toUInt())
        r.setFlagZ(false)
        assertEquals(0b0101_0000u, r.F.toUInt())
        r.setFlagN(false)
        assertEquals(0b0001_0000u, r.F.toUInt())
        r.setFlagC(false)
        assertEquals(0b0000_0000u, r.F.toUInt())

        // Should be NOOPs
        r.setFlagH(false)
        assertEquals(0b0000_0000u, r.F.toUInt())
        r.setFlagZ(false)
        assertEquals(0b0000_0000u, r.F.toUInt())
        r.setFlagN(false)
        assertEquals(0b0000_0000u, r.F.toUInt())
        r.setFlagC(false)
        assertEquals(0b0000_0000u, r.F.toUInt())

        // Check that lower bits aren't affected
        r.F = 0b0000_1111u
        r.setFlagH(false)
        r.setFlagZ(false)
        r.setFlagN(false)
        r.setFlagC(false)
        assertEquals(0b0000_1111u, r.F.toUInt())
    }

    @Test fun testFlagSetterIdempotent() {
        r.setFlagZ(true)
        assertEquals(0b1000_0000u, r.F.toUInt())
        r.setFlagZ(true)
        r.setFlagZ(true)
        assertEquals(0b1000_0000u, r.F.toUInt())
        r.setFlagZ(false)
        assertEquals(0b0000_0000u, r.F.toUInt())
        r.setFlagZ(false)
        r.setFlagZ(false)
        assertEquals(0b0000_0000u, r.F.toUInt())
    }

    @Test fun testGetterSetterIntegTests() {
        r.setAF(0xFFFFu)
        assertTrue(r.flagZ())
        assertTrue(r.flagH())
        assertTrue(r.flagN())
        assertTrue(r.flagC())

        r.setFlagZ(false)
        assertFalse(r.flagZ())
        assertTrue(r.flagH())
        assertTrue(r.flagN())
        assertTrue(r.flagC())

        r.setFlagN(false)
        assertFalse(r.flagZ())
        assertTrue(r.flagH())
        assertFalse(r.flagN())
        assertTrue(r.flagC())

        r.setFlagH(false)
        assertFalse(r.flagZ())
        assertFalse(r.flagH())
        assertFalse(r.flagN())
        assertTrue(r.flagC())

        r.setFlagC(false)
        assertFalse(r.flagZ())
        assertFalse(r.flagH())
        assertFalse(r.flagN())
        assertFalse(r.flagC())

        r.setFlagC(true)
        assertTrue(r.flagC())
        r.setFlagH(true)
        assertTrue(r.flagH())
        r.setFlagZ(true)
        assertTrue(r.flagZ())
        r.setFlagN(true)
        assertTrue(r.flagN())
    }
}