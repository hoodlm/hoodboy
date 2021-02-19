import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InstructionPushPopTests: InstructionTestBase() {
    @Test fun testPushPopBC() {
        r.setSP(0x8000u)
        r.setBC(0x1C2Du)
        InstructionPushBCToStack().invoke(r, m, DATA)
        assertEquals(0x7FFEu, r.SP().toUInt())
        assertEquals(0x1Cu, m.getByte(0x7FFFu).toUInt())
        assertEquals(0x2Du, m.getByte(0x7FFEu).toUInt())
        r.setBC(0u)

        InstructionPopBCFromStack().invoke(r, m, DATA)
        assertEquals(0x8000u, r.SP().toUInt())
        assertEquals(0x1C2Du, r.BC().toUInt())

        r.setSP(0u)
        r.setBC(0u)
        r.assertZeroed()
    }

    @Test fun testPushPopAF() {
        r.setSP(0x7000u)
        r.setAF(0x1C2Du)
        InstructionPushAFToStack().invoke(r, m, DATA)
        assertEquals(0x6FFEu, r.SP().toUInt())
        assertEquals(0x1Cu, m.getByte(0x6FFFu).toUInt())
        assertEquals(0x2Du, m.getByte(0x6FFEu).toUInt())
        r.setAF(0u)

        InstructionPopAFFromStack().invoke(r, m, DATA)
        assertEquals(0x7000u, r.SP().toUInt())
        assertEquals(0x1C2Du, r.AF().toUInt())

        r.setSP(0u)
        r.setAF(0u)
        r.assertZeroed()
    }

    @Test fun testPushPopDE() {
        r.setSP(0x9000u)
        r.setDE(0x1C2Du)
        InstructionPushDEToStack().invoke(r, m, DATA)
        assertEquals(0x8FFEu, r.SP().toUInt())
        assertEquals(0x1Cu, m.getByte(0x8FFFu).toUInt())
        assertEquals(0x2Du, m.getByte(0x8FFEu).toUInt())
        r.setDE(0u)

        InstructionPopDEFromStack().invoke(r, m, DATA)
        assertEquals(0x9000u, r.SP().toUInt())
        assertEquals(0x1C2Du, r.DE().toUInt())

        r.setSP(0u)
        r.setDE(0u)
        r.assertZeroed()
    }

    @Test fun testPushPopHL() {
        r.setSP(0xB000u)
        r.setHL(0x1C2Du)
        InstructionPushHLToStack().invoke(r, m, DATA)
        assertEquals(0xAFFEu, r.SP().toUInt())
        assertEquals(0x1Cu, m.getByte(0xAFFFu).toUInt())
        assertEquals(0x2Du, m.getByte(0xAFFEu).toUInt())
        r.setHL(0u)

        InstructionPopHLFromStack().invoke(r, m, DATA)
        assertEquals(0xB000u, r.SP().toUInt())
        assertEquals(0x1C2Du, r.HL().toUInt())

        r.setSP(0u)
        r.setHL(0u)
        r.assertZeroed()
    }

    @Test fun pushMany() {
        val topOfStack: UShort = 0xB000u
        val iterations = 0x0AFFu
        r.setSP(topOfStack)

        for (n in 1u..iterations) {
            r.setBC(n.toUShort())
            InstructionPushBCToStack().invoke(r, m, DATA)
            val expectedStackPointer = (topOfStack - (2u*n)).toUShort()
            assertEquals(expectedStackPointer, r.SP())
        }
        for (n in 1u..iterations) {
            InstructionPopDEFromStack().invoke(r, m, DATA)
            assertEquals(r.DE(), (iterations.inc() - n).toUShort())
        }
        assertEquals(topOfStack, r.SP())
        r.setDE(0u)
        r.setSP(0u)
        r.setBC(0u)
        r.assertZeroed()
    }
}