import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.AssertionError

open class InstructionTestBase {
    val r = Registers()
    val m = Memory()

    val BYTE_VALUE_ZERO: UByte = 0x00u
    val BYTE_0: UByte = 0x11u
    val BYTE_1: UByte = 0x22u
    val BYTE_2: UByte = 0x33u
    val BYTE_3: UByte = 0x44u
    val DATA: List<UByte> = listOf(BYTE_0, BYTE_1, BYTE_2, BYTE_3)

    val NO_DATA: List<UByte> = listOf()
    val ONE_BYTE_DATA: List<UByte> = listOf(BYTE_1)

    @BeforeEach
    fun reset() {
        r.clear()
        r.assertZeroed()
    }

    fun Registers.assertZeroed() {
        this.assertZeroedIgnoreFlags()
        assertEquals(this.F.toUInt(), 0u)
    }

    fun Registers.assertZeroedIgnoreFlags() {
        val zeroByte: UByte = 0u
        val zeroShort: UShort = 0u
        listOf(
            this.A, this.B, this.C, this.D, this.E, this.H, this.L
        ).forEach { bit8Register ->
            assertEquals(zeroByte, bit8Register, "Registers not zeroed: ${this.dumpRegisters()}")
        }
        listOf(this.PC(), this.SP(), this.BC(), this.DE(), this.HL()).forEach { bit16Register ->
            assertEquals(zeroShort, bit16Register, "Registers not zeroed: ${this.dumpRegisters()}")
        }
    }
}