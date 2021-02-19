import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InstructionLoadFromMemoryToRegisterTests: InstructionTestBase() {
    @Test fun testFromHLtoA() {
        val address: UShort = 0xABCDu
        m.putByte(address, BYTE_1)

        r.setHL(address)
        assertEquals(BYTE_VALUE_ZERO, r.A)
        InstructionLoadFromHLAddressToA().invoke(r, m, DATA)
        assertEquals(BYTE_1, r.A)
        assertEquals(address, r.HL())
        r.A = 0u
        r.setHL(0u)
        r.assertZeroed()

        r.setHL(address)
        assertEquals(BYTE_VALUE_ZERO, r.A)
        InstructionLoadFromHLAddressToAIncrement().invoke(r, m, DATA)
        assertEquals(BYTE_1, r.A)
        assertEquals(address + 1u, r.HL().toUInt())
        r.A = 0u
        r.setHL(0u)
        r.assertZeroed()

        r.setHL(address)
        assertEquals(BYTE_VALUE_ZERO, r.A)
        InstructionLoadFromHLAddressToADecrement().invoke(r, m, DATA)
        assertEquals(BYTE_1, r.A)
        assertEquals(address - 1u, r.HL().toUInt())
        r.A = 0u
        r.setHL(0u)
        r.assertZeroed()
    }

    @Test fun testFromDEtoA() {
        val address: UShort = 0x2468u
        m.putByte(address, BYTE_1)

        r.setDE(address)
        assertEquals(BYTE_VALUE_ZERO, r.A)
        InstructionLoadFromDEAddressToA().invoke(r, m, DATA)
        assertEquals(BYTE_1, r.A)
        assertEquals(address, r.DE())
        r.A = 0u
        r.setDE(0u)
        r.assertZeroed()
    }

    @Test fun testFromBCtoA() {
        val address: UShort = 0x1347u
        m.putByte(address, BYTE_3)

        r.setBC(address)
        assertEquals(BYTE_VALUE_ZERO, r.A)
        InstructionLoadFromBCAddressToA().invoke(r, m, DATA)
        assertEquals(BYTE_3, r.A)
        assertEquals(address, r.BC())
        r.A = 0u
        r.setBC(0u)
        r.assertZeroed()
    }
}