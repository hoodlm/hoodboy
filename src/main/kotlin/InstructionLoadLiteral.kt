import kotlin.reflect.KMutableProperty

interface InstructionLoadLiteral: Instruction {
    fun registersToLoad(registers: Registers): List<KMutableProperty<UByte>>

    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        val targetRegisters = registersToLoad(registers)
        assert(targetRegisters.size <= 3)
        assert(immediateData.size == 4)
        targetRegisters.forEachIndexed { index, register ->
            register.setter.call(immediateData.toList()[index + 1])
        }
    }
}

interface InstructionLoad8BitLiteral: InstructionLoadLiteral {
    override val size: UShort
        get() = 2u
}

class InstructionLoadBd8(): InstructionLoad8BitLiteral {
    override fun registersToLoad(registers: Registers) = listOf(registers::B)
}

class InstructionLoadDd8(): InstructionLoad8BitLiteral {
    override fun registersToLoad(registers: Registers) = listOf(registers::D)
}

class InstructionLoadHd8(): InstructionLoad8BitLiteral {
    override fun registersToLoad(registers: Registers) = listOf(registers::H)
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