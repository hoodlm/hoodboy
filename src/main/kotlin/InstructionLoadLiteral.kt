import kotlin.reflect.KMutableProperty

interface InstructionLoadLiteral: Instruction {
    fun registersToLoad(registers: Registers): List<KMutableProperty<UByte>>

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        val targetRegisters = registersToLoad(registers)
        assert(targetRegisters.size <= 3)
        assert(immediateData.size == 4)
        targetRegisters.forEachIndexed { index, register ->
            register.setter.call(immediateData[index + 1])
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


interface InstructionLoad16BitLiteralDoubleRegister: InstructionLoadLiteral {
    override val size: UShort
        get() = 3u
}

class InstructionLoadBCd16(): InstructionLoad16BitLiteralDoubleRegister {
    override fun registersToLoad(registers: Registers) = listOf(registers::B, registers::C)
}

class InstructionLoadDEd16(): InstructionLoad16BitLiteralDoubleRegister {
    override fun registersToLoad(registers: Registers) = listOf(registers::D, registers::E)
}

class InstructionLoadHLd16(): InstructionLoad16BitLiteralDoubleRegister {
    override fun registersToLoad(registers: Registers) = listOf(registers::H, registers::L)
}


interface InstructionLoad16BitLiteralWideRegister: Instruction {
    override val size: UShort
        get() = 3u

    fun registerToLoad(registers: Registers): KMutableProperty<UShort>

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        val targetRegister = registerToLoad(registers)
        assert(immediateData.size == 4)
        val bytes: Pair<UByte, UByte> = Pair(immediateData[1], immediateData[2])
        targetRegister.setter.call(bytes.toUShort())
    }
}

class InstructionLoadSPd16(): InstructionLoad16BitLiteralWideRegister {
    override fun registerToLoad(registers: Registers) = registers::SP
}