import kotlin.reflect.KMutableProperty

interface InstructionIncrementTwoByteRegister: Instruction {
    override val size: UShort
        get() = 1u

    fun currentRegisterValue(registers: Registers): UShort
    fun setNewRegisterValue(registers: Registers, newValue: UShort)

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
        val currentValue = currentRegisterValue(registers)
        setNewRegisterValue(registers, currentValue.inc())

        registers.setFlagN(false)
        registers.setFlagZ(currentValue == UShort.MAX_VALUE)
        registers.setFlagH(wasHalfCarried(currentValue, currentValue.inc()))
    }
}

class InstructionIncrementBC: InstructionIncrementTwoByteRegister {
    override fun currentRegisterValue(registers: Registers): UShort = registers.BC()
    override fun setNewRegisterValue(registers: Registers, newValue: UShort) = registers.setBC(newValue)
}

class InstructionIncrementDE: InstructionIncrementTwoByteRegister {
    override fun currentRegisterValue(registers: Registers): UShort = registers.DE()
    override fun setNewRegisterValue(registers: Registers, newValue: UShort) = registers.setDE(newValue)
}

class InstructionIncrementHL: InstructionIncrementTwoByteRegister {
    override fun currentRegisterValue(registers: Registers): UShort = registers.HL()
    override fun setNewRegisterValue(registers: Registers, newValue: UShort) = registers.setHL(newValue)
}

class InstructionIncrementSP: InstructionIncrementTwoByteRegister {
    override fun currentRegisterValue(registers: Registers): UShort = registers.SP()
    override fun setNewRegisterValue(registers: Registers, newValue: UShort) = registers.setSP(newValue)
}

interface InstructionDecrementTwoByteRegister: Instruction {
    override val size: UShort
        get() = 1u

    fun currentRegisterValue(registers: Registers): UShort
    fun setNewRegisterValue(registers: Registers, newValue: UShort)

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
        val currentValue = currentRegisterValue(registers)
        setNewRegisterValue(registers, currentValue.dec())

        registers.setFlagN(true)
        registers.setFlagZ(currentValue == UShort.MAX_VALUE)
        registers.setFlagH(wasHalfCarried(currentValue, currentValue.dec()))
    }
}

class InstructionDecrementBC: InstructionDecrementTwoByteRegister {
    override fun currentRegisterValue(registers: Registers): UShort = registers.BC()
    override fun setNewRegisterValue(registers: Registers, newValue: UShort) = registers.setBC(newValue)
}

class InstructionDecrementDE: InstructionDecrementTwoByteRegister {
    override fun currentRegisterValue(registers: Registers): UShort = registers.DE()
    override fun setNewRegisterValue(registers: Registers, newValue: UShort) = registers.setDE(newValue)
}

class InstructionDecrementHL: InstructionDecrementTwoByteRegister {
    override fun currentRegisterValue(registers: Registers): UShort = registers.HL()
    override fun setNewRegisterValue(registers: Registers, newValue: UShort) = registers.setHL(newValue)
}

class InstructionDecrementSP: InstructionDecrementTwoByteRegister {
    override fun currentRegisterValue(registers: Registers): UShort = registers.SP()
    override fun setNewRegisterValue(registers: Registers, newValue: UShort) = registers.setSP(newValue)
}

interface InstructionIncrementByteRegister: Instruction {
    override val size: UShort
        get() = 1u

    fun registerToIncrement(registers: Registers): KMutableProperty<UByte>
    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
        val targetRegister = registerToIncrement(registers)
        val currentValue = targetRegister.getter.call()
        targetRegister.setter.call(currentValue.inc())

        registers.setFlagN(false)
        registers.setFlagZ(currentValue == UByte.MAX_VALUE)
        registers.setFlagH(wasHalfCarried(currentValue, currentValue.inc()))
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
    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
        val targetRegister = registerToDecrement(registers)
        val currentValue = targetRegister.getter.call()
        val result = currentValue.dec()
        targetRegister.setter.call(result)

        registers.setFlagN(true)
        registers.setFlagZ(result == UByte.MIN_VALUE)
        registers.setFlagH(wasHalfCarried(currentValue, currentValue.dec()))
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