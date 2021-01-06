import kotlin.reflect.KMutableProperty

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

interface InstructionLoadLiteral: Instruction {
    fun registersToLoad(registers: Registers): List<KMutableProperty<UByte>>

    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        val targetRegisters = registersToLoad(registers)
        assert(immediateData.size == targetRegisters.size)
        targetRegisters.forEachIndexed { index, register ->
            register.setter.call(immediateData.toList().get(index))
        }
    }
}

interface InstructionLoad16BitLiteral: InstructionLoadLiteral {
    override val size: UShort
        get() = 3u
}

class InstructionLoadBCd16(): InstructionLoad16BitLiteral {
    override fun registersToLoad(registers: Registers) = listOf(registers::B, registers::C)
}

class InstructionLoadDEd16(): InstructionLoad16BitLiteral {
    override fun registersToLoad(registers: Registers) = listOf(registers::D, registers::E)
}

class InstructionLoadHLd16(): InstructionLoad16BitLiteral {
    override fun registersToLoad(registers: Registers) = listOf(registers::H, registers::L)
}