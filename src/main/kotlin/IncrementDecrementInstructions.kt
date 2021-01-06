import kotlin.reflect.KMutableProperty

class InstructionIncrementBC: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        registers.setBC(
            registers.BC().inc()
        )
    }
}

class InstructionIncrementDE: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        registers.setDE(
            registers.DE().inc()
        )
    }
}

class InstructionIncrementHL: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        registers.setHL(
            registers.HL().inc()
        )
    }
}

class InstructionIncrementSP: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        registers.SP = registers.SP.inc()
    }
}

class InstructionDecrementBC: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        registers.setBC(
            registers.BC().dec()
        )
    }
}

class InstructionDecrementDE: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        registers.setDE(
            registers.DE().dec()
        )
    }
}

class InstructionDecrementHL: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        registers.setHL(
            registers.HL().dec()
        )
    }
}

class InstructionDecrementSP: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        registers.SP = registers.SP.dec()
    }
}

interface InstructionIncrementByteRegister: Instruction {
    override val size: UShort
        get() = 1u

    fun registerToIncrement(registers: Registers): KMutableProperty<UByte>
    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        val targetRegister = registerToIncrement(registers)
        val currentValue = targetRegister.getter.call()
        targetRegister.setter.call(currentValue.inc())
    }
}

class InstructionIncrementA: InstructionIncrementByteRegister {
    override fun registerToIncrement(registers: Registers) = registers::A
}

class InstructionIncrementB: InstructionIncrementByteRegister {
    override fun registerToIncrement(registers: Registers) = registers::B
}

class InstructionIncrementC: InstructionIncrementByteRegister {
    override fun registerToIncrement(registers: Registers) = registers::C
}

class InstructionIncrementD: InstructionIncrementByteRegister {
    override fun registerToIncrement(registers: Registers) = registers::D
}

class InstructionIncrementE: InstructionIncrementByteRegister {
    override fun registerToIncrement(registers: Registers) = registers::E
}

class InstructionIncrementF: InstructionIncrementByteRegister {
    override fun registerToIncrement(registers: Registers) = registers::F
}

class InstructionIncrementH: InstructionIncrementByteRegister {
    override fun registerToIncrement(registers: Registers) = registers::H
}

class InstructionIncrementL: InstructionIncrementByteRegister {
    override fun registerToIncrement(registers: Registers) = registers::L
}

interface InstructionDecrementByteRegister: Instruction {
    override val size: UShort
        get() = 1u

    fun registerToDecrement(registers: Registers): KMutableProperty<UByte>
    override fun invoke(registers: Registers, memory: Memory, immediateData: Collection<UByte>) {
        val targetRegister = registerToDecrement(registers)
        val currentValue = targetRegister.getter.call()
        targetRegister.setter.call(currentValue.dec())
    }
}

class InstructionDecrementA: InstructionDecrementByteRegister {
    override fun registerToDecrement(registers: Registers) = registers::A
}

class InstructionDecrementB: InstructionDecrementByteRegister {
    override fun registerToDecrement(registers: Registers) = registers::B
}

class InstructionDecrementC: InstructionDecrementByteRegister {
    override fun registerToDecrement(registers: Registers) = registers::C
}

class InstructionDecrementD: InstructionDecrementByteRegister {
    override fun registerToDecrement(registers: Registers) = registers::D
}

class InstructionDecrementE: InstructionDecrementByteRegister {
    override fun registerToDecrement(registers: Registers) = registers::E
}

class InstructionDecrementF: InstructionDecrementByteRegister {
    override fun registerToDecrement(registers: Registers) = registers::F
}

class InstructionDecrementH: InstructionDecrementByteRegister {
    override fun registerToDecrement(registers: Registers) = registers::H
}

class InstructionDecrementL: InstructionDecrementByteRegister {
    override fun registerToDecrement(registers: Registers) = registers::L
}