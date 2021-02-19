interface InstructionRotateBase: Instruction {
    override val size: UShort
        get() = 2u

    fun getValue(r: Registers): UByte
    fun setValue(r: Registers, v: UByte)


    override fun invoke(r: Registers, m: Memory, data: List<UByte>) {
        val currentValue = getValue(r)
        // Stash the current Carry flag value; we'll use this for bit 0 later:
        val previousCarryFlag = r.flagC()
        setCarryFlag(currentValue, r)
        val rotated = doRotation(currentValue)
        val finalValue = handlePreviousCarryFlag(rotated, previousCarryFlag)
        setValue(r, finalValue)
        setFinalFlags(finalValue, r)
    }

    /**
     * If this rotate instruction should update the carry flag, implement that logic here.
     * For example, the standard RL instruction copies the input value's bit 7 into the carry flag.
     */
    fun setCarryFlag(input: UByte, r: Registers) {
        // NOOP by default
    }

    /**
     * After setCarryFlag but before handlePreviousCarryFlag, perform the actual R or L rotation.
     */
    fun doRotation(input: UByte): UByte

    /**
     * If the previous value of the Carry flag affects the output of this instruction,
     * implement that logic here. For example, for standard RL instructions, the previous
     * carry flag value is copied into bit 0.
     */
    fun handlePreviousCarryFlag(value: UByte, previousFlag: Boolean): UByte {
        return value // NOOP by default
    }

    /**
     * After rotation is done, set H, N, Z flags.
     */
    fun setFinalFlags(finalValue: UByte, r: Registers) {
        // NOOP by default
    }
}

// Base implementation for RL and RR instructions
interface InstructionRotateLeftBase: InstructionRotateBase {
    override fun doRotation(input: UByte): UByte {
        // Clear bit 7:
        val preRotate = input.and(0b0111_1111u)
        // Assertion is to make sure we don't overflow:
        assert(preRotate < 0b1000_0000u)
        // Then a leftshift is just a multiplication by 2.
        val rotated = preRotate * 2u
        // Again, an assertion, to make sure there was no overflow:
        assert(rotated <= 0xFFu)
        return rotated.toUByte()
    }
}

interface InstructionRotateLeft: InstructionRotateLeftBase {
    override fun handlePreviousCarryFlag(value: UByte, previousFlag: Boolean): UByte {
        return if (previousFlag) {
            // "The previous value of the carry flag are copied to bit 0 of the register"
            value.xor(0b0000_0001u)
        } else {
            value
        }
    }

    /**
     * Pass the current register value; will set carry flag depending on instruction type.
     */
    override fun setCarryFlag(input: UByte, r: Registers) {
        // Carry flag will contain the old value of bit 7:
        r.setFlagC(BYTE_VALUE_ZERO != input.and(0b1000_0000u))
    }

    override fun doRotation(input: UByte): UByte {
        // Clear bit 7:
        val preRotate = input.and(0b0111_1111u)
        // Assertion is to make sure we don't overflow:
        assert(preRotate < 0b1000_0000u)
        // Then a leftshift is just a multiplication by 2.
        val rotated = preRotate * 2u
        // Again, an assertion, to make sure there was no overflow:
        assert(rotated <= 0xFFu)
        return rotated.toUByte()
    }

    /**
     * Set N/Z/H flags depending on instruction type.
     */
    override fun setFinalFlags(result: UByte, r: Registers) {
        if (BYTE_VALUE_ZERO == result) {
            r.setFlagZ(true)
        } else {
            r.setFlagZ(false)
        }
        r.setFlagN(false)
        r.setFlagH(false)
    }

}

class InstructionRotateLeftA: InstructionRotateLeft {
    override fun getValue(r: Registers) = r.A
    override fun setValue(r: Registers, v: UByte) {
        r.A = v
    }
}

class InstructionRotateLeftB: InstructionRotateLeft {
    override fun getValue(r: Registers) = r.B
    override fun setValue(r: Registers, v: UByte) {
        r.B = v
    }
}

class InstructionRotateLeftC: InstructionRotateLeft {
    override fun getValue(r: Registers) = r.C
    override fun setValue(r: Registers, v: UByte) {
        r.C = v
    }
}

class InstructionRotateLeftD: InstructionRotateLeft {
    override fun getValue(r: Registers) = r.D
    override fun setValue(r: Registers, v: UByte) {
        r.D = v
    }
}

class InstructionRotateLeftE: InstructionRotateLeft {
    override fun getValue(r: Registers) = r.E
    override fun setValue(r: Registers, v: UByte) {
        r.E = v
    }
}

class InstructionRotateLeftH: InstructionRotateLeft {
    override fun getValue(r: Registers) = r.H
    override fun setValue(r: Registers, v: UByte) {
        r.H = v
    }
}

class InstructionRotateLeftL: InstructionRotateLeft {
    override fun getValue(r: Registers) = r.L
    override fun setValue(r: Registers, v: UByte) {
        r.L = v
    }
}