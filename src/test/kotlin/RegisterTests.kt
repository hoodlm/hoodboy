import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RegisterTests {
    val r = Registers()
    @BeforeEach
    fun reset() {
        r.clear()
    }

    @Test
    fun testAF() {
        assertEquals(0x00u.toUByte(), r.A)
        assertEquals(0x00u.toUByte(), r.F)

        r.setAF(0xF00Fu)
        assertEquals(0xF0u.toUByte(), r.A)
        assertEquals(0x0Fu.toUByte(), r.F)

        r.setAF(0x0001u)
        assertEquals(0x00u.toUByte(), r.A)
        assertEquals(0x01u.toUByte(), r.F)

        r.setAF(0x0100u)
        assertEquals(0x01u.toUByte(), r.A)
        assertEquals(0x00u.toUByte(), r.F)

        r.setAF(0xFFFFu)
        assertEquals(0xFFu.toUByte(), r.A)
        assertEquals(0xFFu.toUByte(), r.F)

        r.setAF(0x0000u)
        assertEquals(0x00u.toUByte(), r.A)
        assertEquals(0x00u.toUByte(), r.F)
    }

    @Test
    fun testBC() {
        r.setBC(0xABCDu)
        assertEquals(0xAB.toUByte(), r.B)
        assertEquals(0xCD.toUByte(), r.C)
    }

    @Test
    fun testDE() {
        r.setDE(0xABCDu)
        assertEquals(0xAB.toUByte(), r.D)
        assertEquals(0xCD.toUByte(), r.E)
    }

    @Test
    fun testHL() {
        r.setHL(0xABCDu)
        assertEquals(0xAB.toUByte(), r.H)
        assertEquals(0xCD.toUByte(), r.L)
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
}