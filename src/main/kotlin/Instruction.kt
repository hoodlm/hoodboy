import kotlin.reflect.KMutableProperty

interface Instruction : (Registers, Memory, List<UByte>) -> Unit {
    /**
     * Size of the instruction in bytes
     */
    val size: UShort
}

class InstructionNOOP(): Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
    }
}