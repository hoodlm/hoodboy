interface Instruction : (Registers, Memory, Collection<UByte>) -> Unit {
    /**
     * Size of the instruction in bytes
     */
    val size: UShort
}

class InstructionNOOP(): Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        // NOOP
    }
}

class InstructionLoadBCd16(): Instruction {
    override val size: UShort = 3u
    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        assert(immediateData.size == 2)
        immediateData.toList().also { data ->
            registers.B = data[0]
            registers.C = data[1]
        }
    }
}