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

    @Test fun testLoadBC16dNegative() {
        val instruction = InstructionLoadBCd16()
        listOf(NO_DATA, ONE_BYTE_DATA, THREE_BYTE_DATA).forEach {
            assertThrows(AssertionError::class.java) {
                instruction.invoke(r, m, it)
            }
        }
    }

    @Test fun testLoadBC16dHappy() {
        val instruction = InstructionLoadBCd16()
        instruction.invoke(r, m, TWO_BYTE_DATA)
        assertEquals(r.B, BYTE_1)
        assertEquals(r.C, BYTE_2)
        // if we reset those two registers, all registers should be back to zero
        r.B = 0u
        r.C = 0u
        r.assertZeroed()
    }

    @Test fun testLoadDE16dHappy() {
        val instruction = InstructionLoadDEd16()
        instruction.invoke(r, m, TWO_BYTE_DATA)
        assertEquals(r.D, BYTE_1)
        assertEquals(r.E, BYTE_2)
        // if we reset those two registers, all registers should be back to zero
        r.D = 0u
        r.E = 0u
        r.assertZeroed()
    }

    @Test fun testLoadHL16dHappy() {
        val instruction = InstructionLoadHLd16()
        instruction.invoke(r, m, TWO_BYTE_DATA)
        assertEquals(r.H, BYTE_1)
        assertEquals(r.L, BYTE_2)
        // if we reset those two registers, all registers should be back to zero
        r.H = 0u
        r.L = 0u
        r.assertZeroed()
    }

    @Test fun testIncrementDecrementSingleByteRegisters() {
        r.B = 12u
        r.D = 22u
        r.H = 32u
        InstructionIncrementB().invoke(r, m, NO_DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(22, r.D.toInt())
        assertEquals(32, r.H.toInt())
        InstructionIncrementB().invoke(r, m, NO_DATA)
        assertEquals(14, r.B.toInt())
        assertEquals(22, r.D.toInt())
        assertEquals(32, r.H.toInt())
        InstructionDecrementB().invoke(r, m, NO_DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(22, r.D.toInt())
        assertEquals(32, r.H.toInt())

        InstructionIncrementD().invoke(r, m, NO_DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(23, r.D.toInt())
        assertEquals(32, r.H.toInt())
        InstructionIncrementD().invoke(r, m, NO_DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(24, r.D.toInt())
        assertEquals(32, r.H.toInt())
        InstructionDecrementD().invoke(r, m, NO_DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(23, r.D.toInt())
        assertEquals(32, r.H.toInt())

        InstructionIncrementH().invoke(r, m, NO_DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(23, r.D.toInt())
        assertEquals(33, r.H.toInt())
        InstructionIncrementH().invoke(r, m, NO_DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(23, r.D.toInt())
        assertEquals(34, r.H.toInt())
        InstructionDecrementH().invoke(r, m, NO_DATA)
        assertEquals(13, r.B.toInt())
        assertEquals(23, r.D.toInt())
        assertEquals(33, r.H.toInt())

        for (i in 1..13) {
            InstructionDecrementB().invoke(r, m, NO_DATA)
        }
        for (i in 1..23) {
            InstructionDecrementD().invoke(r, m, NO_DATA)
        }
        for (i in 1..33) {
            InstructionDecrementH().invoke(r, m, NO_DATA)
        }
        r.assertZeroed()
    }


    @Test fun testIncrementDecrementDoubleRegisters() {
        val zero: UShort = 0u
        val max: UShort = 0xFEDCu
        // count up for each of BC, DE, HL, SP
        for (i in 1..max.toInt()) {
            assertEquals((i - 1).toUShort(), r.BC())
            InstructionIncrementBC().invoke(r, m, NO_DATA)
            assertEquals(i.toUShort(), r.BC())
            assertEquals(zero, r.DE())
            assertEquals(zero, r.HL())
            assertEquals(zero, r.SP)
        }
        for (i in 1..max.toInt()) {
            assertEquals((i - 1).toUShort(), r.DE())
            InstructionIncrementDE().invoke(r, m, NO_DATA)
            assertEquals(i.toUShort(), r.DE())
            assertEquals(max, r.BC())
            assertEquals(zero, r.HL())
            assertEquals(zero, r.SP)
        }
        for (i in 1..max.toInt()) {
            assertEquals((i - 1).toUShort(), r.HL())
            InstructionIncrementHL().invoke(r, m, NO_DATA)
            assertEquals(i.toUShort(), r.HL())
            assertEquals(max, r.BC())
            assertEquals(max, r.DE())
            assertEquals(zero, r.SP)
        }
        for (i in 1..max.toInt()) {
            assertEquals((i - 1).toUShort(), r.SP)
            InstructionIncrementSP().invoke(r, m, NO_DATA)
            assertEquals(i.toUShort(), r.SP)
            assertEquals(max, r.BC())
            assertEquals(max, r.DE())
            assertEquals(max, r.HL())
        }
        // count down for each of BC, DE, HL, SP
        for (i in 1..max.toInt()) {
            assertEquals((max.toInt() - i + 1).toUShort(), r.BC())
            InstructionDecrementBC().invoke(r, m, NO_DATA)
            assertEquals((max.toInt() - i).toUShort(), r.BC())
            assertEquals(max, r.DE())
            assertEquals(max, r.HL())
            assertEquals(max, r.SP)
        }
        for (i in 1..max.toInt()) {
            assertEquals((max.toInt() - i + 1).toUShort(), r.DE())
            InstructionDecrementDE().invoke(r, m, NO_DATA)
            assertEquals((max.toInt() - i).toUShort(), r.DE())
            assertEquals(zero, r.BC())
            assertEquals(max, r.HL())
            assertEquals(max, r.SP)
        }
        for (i in 1..max.toInt()) {
            assertEquals((max.toInt() - i + 1).toUShort(), r.HL())
            InstructionDecrementHL().invoke(r, m, NO_DATA)
            assertEquals((max.toInt() - i).toUShort(), r.HL())
            assertEquals(zero, r.BC())
            assertEquals(zero, r.DE())
            assertEquals(max, r.SP)
        }
        for (i in 1..max.toInt()) {
            assertEquals((max.toInt() - i + 1).toUShort(), r.SP)
            InstructionDecrementSP().invoke(r, m, NO_DATA)
            assertEquals((max.toInt() - i).toUShort(), r.SP)
            assertEquals(zero, r.BC())
            assertEquals(zero, r.DE())
            assertEquals(zero, r.HL())
        }
        // finally make sure everything is zero
        r.assertZeroed()
    }

    private fun Registers.assertZeroed() {
        val zeroByte: UByte = 0u
        val zeroShort: UShort = 0u
        listOf(
            this.A, this.B, this.C, this.D, this.E, this.F, this.H, this.L
        ).forEach { bit8Register ->
            assertEquals(zeroByte, bit8Register, "Registers not zeroed: ${this.dumpRegisters()}")
        }
        listOf(this.PC, this.SP).forEach { bit16Register ->
            assertEquals(zeroShort, bit16Register, "Registers not zeroed: ${this.dumpRegisters()}")
        }
    }
}