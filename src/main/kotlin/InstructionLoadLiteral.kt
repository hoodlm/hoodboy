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

class InstructionLoadAd8(): InstructionLoad8BitLiteral {
    override fun registersToLoad(registers: Registers) = listOf(registers::A)
}

class InstructionLoadBd8(): InstructionLoad8BitLiteral {
    override fun registersToLoad(registers: Registers) = listOf(registers::B)
}

class InstructionLoadCd8(): InstructionLoad8BitLiteral {
    override fun registersToLoad(registers: Registers) = listOf(registers::C)
}

class InstructionLoadDd8(): InstructionLoad8BitLiteral {
    override fun registersToLoad(registers: Registers) = listOf(registers::D)
}

class InstructionLoadEd8(): InstructionLoad8BitLiteral {
    override fun registersToLoad(registers: Registers) = listOf(registers::E)
}

class InstructionLoadHd8(): InstructionLoad8BitLiteral {
    override fun registersToLoad(registers: Registers) = listOf(registers::H)
}

class InstructionLoadLd8(): InstructionLoad8BitLiteral {
    override fun registersToLoad(registers: Registers) = listOf(registers::L)
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

    fun setNewRegisterValue(registers: Registers, newValue: UShort)

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(immediateData.size == 4)
        val bytes: Pair<UByte, UByte> = Pair(immediateData[1], immediateData[2])
        setNewRegisterValue(registers, bytes.toUShort())
    }
}

class InstructionLoadSPd16(): InstructionLoad16BitLiteralWideRegister {
    override fun setNewRegisterValue(registers: Registers, newValue: UShort) {
        registers.setSP(newValue)
    }
}