import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class XORInstructionTests: InstructionTestBase() {
    @Test
    fun testXOR_A() {
        val instruction = InstructionXorA()
        // anything XOR with itself is zero
        for (byte in 0u..255u) {
            r.A = byte.toUByte()
            instruction.invoke(r, m, DATA)
            r.assertZeroed()
        }
    }
}