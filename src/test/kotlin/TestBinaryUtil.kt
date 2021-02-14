import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestBinaryUtil {
    val r = Registers()
    @BeforeEach
    fun reset() {
        r.clear()
    }

    @Test
    fun testAF() {
        assertEquals(0x00u.toUByte(), r.A)
        assertEquals(0x00u.toUByte(), r.F)

        var twobyte: UShort
        twobyte = 0xF00fu
        r.setAF(twobyte)
        assertEquals(0xF0u.toUByte(), r.A)
        assertEquals(0x0Fu.toUByte(), r.F)
        assertEquals(twobyte, r.AF())

        twobyte = 0x0001u
        r.setAF(twobyte)
        assertEquals(0x00u.toUByte(), r.A)
        assertEquals(0x01u.toUByte(), r.F)
        assertEquals(twobyte, r.AF())

        twobyte = 0x0100u
        r.setAF(twobyte)
        assertEquals(0x01u.toUByte(), r.A)
        assertEquals(0x00u.toUByte(), r.F)
        assertEquals(twobyte, r.AF())

        twobyte = 0xFFFFu
        r.setAF(twobyte)
        assertEquals(0xFFu.toUByte(), r.A)
        assertEquals(0xFFu.toUByte(), r.F)
        assertEquals(twobyte, r.AF())

        twobyte = 0x0000u
        r.setAF(twobyte)
        assertEquals(0x00u.toUByte(), r.A)
        assertEquals(0x00u.toUByte(), r.F)
        assertEquals(twobyte, r.AF())
    }

    @Test
    fun testBC() {
        var twobyte: UShort = 0xABCDu
        r.setBC(twobyte)
        assertEquals(0xAB.toUByte(), r.B)
        assertEquals(0xCD.toUByte(), r.C)
        assertEquals(twobyte, r.BC())
    }

    @Test
    fun testDE() {
        var twobyte: UShort = 0xABCDu
        r.setDE(twobyte)
        assertEquals(0xAB.toUByte(), r.D)
        assertEquals(0xCD.toUByte(), r.E)
        assertEquals(twobyte, r.DE())
    }

    @Test
    fun testHL() {
        var twobyte: UShort = 0xABCDu
        r.setHL(twobyte)
        assertEquals(0xAB.toUByte(), r.H)
        assertEquals(0xCD.toUByte(), r.L)
        assertEquals(twobyte, r.HL())
    }

    @Test
    fun test16BitSplitExhaustive() {
        for (i in UShort.MIN_VALUE..UShort.MAX_VALUE) {
            val actual = i.toUShort()
            assertEquals(actual, actual.splitHighAndLowBits().toUShort());
        }
    }

    @Test
    fun test8BitJoinExhaustive() {
        for (x in UByte.MIN_VALUE..UByte.MAX_VALUE) {
            for (y in UByte.MIN_VALUE..UByte.MAX_VALUE) {
                val actual: Pair<UByte, UByte> = Pair(x.toUByte(), y.toUByte())
                assertEquals(actual, actual.toUShort().splitHighAndLowBits())
            }
        }
    }

    @Test
    fun testHalfCarry() {
        // Any number and itself is NOT a half-carry
        for (n in UByte.MIN_VALUE..UByte.MAX_VALUE) {
            assertFalse(wasHalfCarried(n.toUByte(), n.toUByte()))
        }

        // examples of half-carry FALSE
        mapOf(
            0b0001_0000u to 0b0001_0001u,
            0b1111_1111u to 0b0101_0101u,
            0b1110_1111u to 0b0110_1111u
        ).forEach { (x, y) ->
            // should be commutative, and work the same for a UByte or UShort pair:
            assertFalse(wasHalfCarried(x.toUByte(), y.toUByte()))
            assertFalse(wasHalfCarried(y.toUByte(), x.toUByte()))
            assertFalse(wasHalfCarried(x.toUShort(), y.toUShort()))
            assertFalse(wasHalfCarried(y.toUShort(), x.toUShort()))
        }

        // examples of half-carry TRUE
        mapOf(
            0b0001_0000u to 0b0000_0000u,
            0b1111_0000u to 0b1110_1111u,
            0b1110_1111u to 0b1111_1111u
        ).forEach { (x, y) ->
            // should be commutative, and work the same for a UByte or UShort pair:
            assertTrue(wasHalfCarried(x.toUByte(), y.toUByte()))
            assertTrue(wasHalfCarried(y.toUByte(), x.toUByte()))
            assertTrue(wasHalfCarried(x.toUShort(), y.toUShort()))
            assertTrue(wasHalfCarried(y.toUShort(), x.toUShort()))
        }
    }

    @Test
    fun testHalfCarry16bit() {
        // Any number and itself is NOT a half-carry
        for (n in UShort.MIN_VALUE..UShort.MAX_VALUE) {
            assertFalse(wasHalfCarried(n.toUByte(), n.toUByte()))
        }

        // examples of half-carry FALSE
        mapOf(
            0b1111_0000_0001_0000u to 0b1111_1111_0001_0001u,
            0b1111_0000_1111_1111u to 0b0000_0000_0101_0101u,
            0b1010_0101_1110_1111u to 0b0000_0001_0110_1111u
        ).forEach { (x, y) ->
            // should be commutative, and work the same for a UByte or UShort pair:
            assertFalse(wasHalfCarried(x.toUByte(), y.toUByte()))
            assertFalse(wasHalfCarried(y.toUByte(), x.toUByte()))
            assertFalse(wasHalfCarried(x.toUShort(), y.toUShort()))
            assertFalse(wasHalfCarried(y.toUShort(), x.toUShort()))
        }

        // examples of half-carry TRUE
        mapOf(
            0b1111_0000_0001_0000u to 0b0000_0000_0000_0000u,
            0b0101_1010_1111_0000u to 0b1111_1101_1110_1111u,
            0b1111_1111_1110_1111u to 0b1111_1111_1111_1111u
        ).forEach { (x, y) ->
            // should be commutative, and work the same for a UByte or UShort pair:
            assertTrue(wasHalfCarried(x.toUByte(), y.toUByte()))
            assertTrue(wasHalfCarried(y.toUByte(), x.toUByte()))
            assertTrue(wasHalfCarried(x.toUShort(), y.toUShort()))
            assertTrue(wasHalfCarried(y.toUShort(), x.toUShort()))
        }
    }
}