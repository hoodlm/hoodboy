import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StackPointerInstructionTests: InstructionTestBase() {
    @Test fun testCallLiteralAddressAndReturn() {
        val startAddress: UShort = 0xABCDu
        val startAddressAsBytes = startAddress.splitHighAndLowBits()
        val targetAddress: UShort = 0x2468u
        val targetAddressAsBytes = targetAddress.splitHighAndLowBits()
        val topOfStack: UShort = 0x8000u

        r.setPC(startAddress)
        r.setSP(topOfStack)

        val programData = listOf(BYTE_0, targetAddressAsBytes.second, targetAddressAsBytes.first, BYTE_3)
        InstructionCallLiteralAddress().invoke(r, m, programData)
        assertEquals(targetAddress, r.PC())
        assertEquals(topOfStack.dec().dec(), r.SP())
        assertEquals(startAddressAsBytes.first, m.getByte(topOfStack.dec()))
        assertEquals(startAddressAsBytes.second, m.getByte(topOfStack.dec().dec()))

        InstructionReturn().invoke(r, m, DATA)
        assertEquals(startAddress, r.PC())
        assertEquals(topOfStack, r.SP())
        // these should have been the only registers altered:
        r.setPC(0u)
        r.setSP(0u)
        r.assertZeroed()
    }
}