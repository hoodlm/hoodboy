import kotlin.reflect.KMutableProperty

class InstructionIncrementBC: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
        registers.setBC(
            registers.BC().inc()
        )
    }
}

class InstructionIncrementDE: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
        registers.setDE(
            registers.DE().inc()
        )
    }
}

class InstructionIncrementHL: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
        registers.setHL(
            registers.HL().inc()
        )
    }
}

class InstructionIncrementSP: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        registers.SP = registers.SP.inc()
    }
}

class InstructionDecrementBC: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
        registers.setBC(
            registers.BC().dec()
        )
    }
}

class InstructionDecrementDE: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        registers.setDE(
            registers.DE().dec()
        )
    }
}

class InstructionDecrementHL: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
        registers.setHL(
            registers.HL().dec()
        )
    }
}

class InstructionDecrementSP: Instruction {
    override val size: UShort = 1u
    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        registers.SP = registers.SP.dec()
    }
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
        registers.setFlagH(currentValue.inc().wasHalfCarried() && !currentValue.wasHalfCarried())
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
        // "Set if no borrow from bit 4."
        // Examples:
        //   0b0001_0000 -> 0b0000_1111 -> borrow
        //   0b1111_0000 -> 0b1110_1111 -> borrow
        //   0b0000_0010 -> 0b0000_0001 -> no borrow
        //   0b0001_0011 -> 0b0001_0010 -> no borrow
        // decrementing any number that ends in _0000 is going to result in a borrow.
        // HALF_BORROW_MASK is 0b0000_1111
        registers.setFlagH(currentValue.and(HALF_BORROW_MASK) != UByte.MIN_VALUE)
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