class InstructionRegisterBitTest(
    private val bitIndex: Int,
    private val fetchRegisterValue: (Registers) -> UByte
): Instruction {
    override val size: UShort = 2u

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
        assert(bitIndex in 0..7)
        val mask = ONE_BIT_MASKS.get(bitIndex)

        val testResult = mask.and(fetchRegisterValue(registers)).equals(mask)
        registers.setFlagZ(!testResult)
        registers.setFlagN(false)
        registers.setFlagH(true)
    }
}