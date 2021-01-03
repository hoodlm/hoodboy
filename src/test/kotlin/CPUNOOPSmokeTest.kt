import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CPUNOOPSmokeTest {
    @Test
    fun interpretAllAsNoop() {
        val registers = Registers()
        val memory = Memory()
        val cpu = CPU(registers, memory, NOOPInstructionInterpreter())
        memory.assertMemoryZeroes()
        for (byte in 0u..255u) {
            val instruction = byte.toUByte();
            val expectedPCBefore: UShort = instruction.toUShort()
            assertEquals(expectedPCBefore, registers.PC)
            cpu.execInstruction(Pair(instruction, instruction), setOf())
            val expectedPCAfter: UShort = expectedPCBefore.inc()
            assertEquals(expectedPCAfter, registers.PC)
        }
        memory.assertMemoryZeroes()
    }

    private fun Memory.assertMemoryZeroes() {
        val zero: UByte = 0u
        for (i in UShort.MIN_VALUE..UShort.MAX_VALUE) {
            val address = i.toUShort()
            assertEquals(zero, this.getByte(address));
        }
    }
}