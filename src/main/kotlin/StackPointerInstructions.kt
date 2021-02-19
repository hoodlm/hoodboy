class InstructionCallLiteralAddress: Instruction {
    override val size: UShort = 3u

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
        val previousStackPointer = registers.SP()
        val currentAddressBytes = registers.PC().splitHighAndLowBits()
        memory.putByte(previousStackPointer.dec(), currentAddressBytes.first)
        memory.putByte(previousStackPointer.dec().dec(), currentAddressBytes.second)

        val newAddress = Pair(immediateData[2], immediateData[1]).toUShort()
        registers.setPC(newAddress)
        registers.setSP(previousStackPointer.dec().dec())
    }
}

class InstructionReturn: Instruction {
    override val size: UShort = 1u

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
        val previousStackPointer = registers.SP()
        val returnAddress = Pair(
            memory.getByte(previousStackPointer.inc()),
            memory.getByte(previousStackPointer)).toUShort()
        registers.setPC(returnAddress)
        registers.setSP(previousStackPointer.inc().inc())
    }
}