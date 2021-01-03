interface Instruction : (Registers, Memory, Collection<UByte>) -> Unit {
    /**
     * Size of the instruction in bytes
     */
    val size: UShort
}

class NOOPInstruction() : Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        // NOOP
    }
}