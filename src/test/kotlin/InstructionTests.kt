import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.AssertionError

class InstructionTests {
    val r = Registers()
    val m = Memory()

    val BYTE_1: UByte = 0x11u
    val BYTE_2: UByte = 0x22u
    val BYTE_3: UByte = 0x33u
    val NO_DATA: Collection<UByte> = setOf()
    val ONE_BYTE_DATA: Collection<UByte> = listOf(BYTE_1)
    val TWO_BYTE_DATA: Collection<UByte> = listOf(BYTE_1, BYTE_2)
    val THREE_BYTE_DATA: Collection<UByte> = listOf(BYTE_1, BYTE_2, BYTE_3)

    @BeforeEach
    fun reset() {
        r.clear()
        r.assertZeroed()
    }

    @Test fun testNOOP() {
        val instruction = InstructionNOOP()
        listOf(NO_DATA, ONE_BYTE_DATA, TWO_BYTE_DATA, THREE_BYTE_DATA).forEach { immediateData ->
            instruction.invoke(r, m, immediateData)
            r.assertZeroed()
        }
    }

    @Test fun testLoadBC16dHappy() {
        val instruction = InstructionLoadBCd16()
        instruction.invoke(r, m, TWO_BYTE_DATA)
        assertEquals(r.B, BYTE_1)
        assertEquals(r.C, BYTE_2)
    }

    @Test fun testLoadBC16dNegative() {
        val instruction = InstructionLoadBCd16()
        listOf(NO_DATA, ONE_BYTE_DATA, THREE_BYTE_DATA).forEach {
            assertThrows(AssertionError::class.java) {
                instruction.invoke(r, m, it)
            }
        }
    }

    private fun Registers.assertZeroed() {
        val zeroByte: UByte = 0u
        val zeroShort: UShort = 0u
        listOf(
            this.A, this.B, this.C, this.D, this.E, this.F, this.H, this.L
        ).forEach { bit8Register ->
            assertEquals(zeroByte, bit8Register)
        }
        listOf(this.PC, this.SP).forEach { bit16Register ->
            assertEquals(zeroShort, bit16Register)
        }
    }
}