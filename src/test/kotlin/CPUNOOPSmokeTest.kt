import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CPUNOOPSmokeTest {
    @Test
    fun interpretAllAsNoop() {
        val cpu = CPU(Registers(), Memory(), NOOPInstructionInterpreter())
        cpu.assertMemoryZeroes()
        for (byte in 0u..255u) {
            val instruction = byte.toUByte();
            cpu.execInstruction(instruction)
        }
        cpu.assertMemoryZeroes()
    }

    private fun CPU.assertMemoryZeroes() {
        val zero: UByte = 0u
        for (i in UShort.MIN_VALUE..UShort.MAX_VALUE) {
            val address = i.toUShort()
            assertEquals(zero, this.memory.getByte(address));
        }
    }
}