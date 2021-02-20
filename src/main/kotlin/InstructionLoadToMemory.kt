import kotlin.reflect.KMutableProperty

interface InstructionLoadFromRegisterToMemory: Instruction {
    override val size: UShort
        get() = 1u
    fun destinationAddress(registers: Registers, data: List<UByte>): UShort
    fun valueRegister(registers: Registers): KMutableProperty<UByte>
    fun postInvoke(registers: Registers) {
        // Optional post-invoke code hook, e.g. for decrement/increment register instructions like LD (HL-) A
    }

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(immediateData.size == 4)
        val destinationAddress = destinationAddress(registers, immediateData)
        val destinationValue = valueRegister(registers).getter.call()
        memory.putByte(destinationAddress, destinationValue)
        postInvoke(registers)
    }
}

interface InstructionLoadFromRegisterToHL: InstructionLoadFromRegisterToMemory {
    override fun destinationAddress(registers: Registers, data: List<UByte>) = registers.HL()
}

class InstructionLoadFromAToHL: InstructionLoadFromRegisterToHL {
    override fun valueRegister(registers: Registers): KMutableProperty<UByte> = registers::A
}

class InstructionLoadFromBToHL: InstructionLoadFromRegisterToHL {
    override fun valueRegister(registers: Registers): KMutableProperty<UByte> = registers::B
}

class InstructionLoadFromCToHL: InstructionLoadFromRegisterToHL {
    override fun valueRegister(registers: Registers): KMutableProperty<UByte> = registers::C
}

class InstructionLoadFromDToHL: InstructionLoadFromRegisterToHL {
    override fun valueRegister(registers: Registers): KMutableProperty<UByte> = registers::D
}

class InstructionLoadFromEToHL: InstructionLoadFromRegisterToHL {
    override fun valueRegister(registers: Registers): KMutableProperty<UByte> = registers::E
}

class InstructionLoadFromHToHL: InstructionLoadFromRegisterToHL {
    override fun valueRegister(registers: Registers): KMutableProperty<UByte> = registers::H
}

class InstructionLoadFromLToHL: InstructionLoadFromRegisterToHL {
    override fun valueRegister(registers: Registers): KMutableProperty<UByte> = registers::L
}

class InstructionLoadFromAToHLDecrement: InstructionLoadFromRegisterToMemory {
    override fun destinationAddress(registers: Registers, data: List<UByte>) = registers.HL()
    override fun valueRegister(registers: Registers): KMutableProperty<UByte> = registers::A
    override fun postInvoke(registers: Registers) {
        registers.setHL(registers.HL().dec())
    }
}

class InstructionLoadFromAToHLIncrement: InstructionLoadFromRegisterToMemory {
    override fun destinationAddress(registers: Registers, data: List<UByte>) = registers.HL()
    override fun valueRegister(registers: Registers): KMutableProperty<UByte> = registers::A
    override fun postInvoke(registers: Registers) {
        registers.setHL(registers.HL().inc())
    }
}

class InstructionLoadAFromC: InstructionLoadFromRegisterToMemory {
    override fun destinationAddress(registers: Registers, data: List<UByte>): UShort {
        return (0xFF00u + registers.C).toUShort()
    }

    override fun valueRegister(registers: Registers): KMutableProperty<UByte> = registers::A
}

class InstructionLoadAFrom8BitLiteral: InstructionLoadFromRegisterToMemory {
    override val size: UShort
        get() = 2u

    override fun destinationAddress(registers: Registers, data: List<UByte>): UShort {
        return (0xFF00u + data[1]).toUShort()
    }

    override fun valueRegister(registers: Registers): KMutableProperty<UByte> = registers::A
}

class InstructionLoadAFrom16BitLiteral: InstructionLoadFromRegisterToMemory {
    override val size: UShort
        get() = 3u

    override fun destinationAddress(registers: Registers, data: List<UByte>): UShort {
        return Pair(data[2], data[1]).toUShort()
    }

    override fun valueRegister(registers: Registers): KMutableProperty<UByte> = registers::A
}