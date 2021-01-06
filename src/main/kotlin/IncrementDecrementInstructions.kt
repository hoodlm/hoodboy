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

class InstructionIncrementB: InstructionIncrementByteRegister {
    override fun registerToIncrement(registers: Registers) = registers::B
}

class InstructionIncrementD: InstructionIncrementByteRegister {
    override fun registerToIncrement(registers: Registers) = registers::D
}

class InstructionIncrementH: InstructionIncrementByteRegister {
    override fun registerToIncrement(registers: Registers) = registers::H
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

class InstructionDecrementB: InstructionDecrementByteRegister {
    override fun registerToDecrement(registers: Registers) = registers::B
}

class InstructionDecrementD: InstructionDecrementByteRegister {
    override fun registerToDecrement(registers: Registers) = registers::D
}

class InstructionDecrementH: InstructionDecrementByteRegister {
    override fun registerToDecrement(registers: Registers) = registers::H
}