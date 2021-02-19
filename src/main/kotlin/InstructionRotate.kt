interface InstructionRotateLeft: Instruction {
    override val size: UShort
        get() = 2u

    fun getValue(r: Registers): UByte
    fun setValue(r: Registers, v: UByte)

    override fun invoke(r: Registers, m: Memory, data: List<UByte>) {
        val currentValue = getValue(r)
        // Stash the current Carry flag value; we'll use this for bit 0 later:
        val previousCarryFlag = r.flagC()
        // Carry flag will contain the old value of bit 7:
        r.setFlagC(BYTE_VALUE_ZERO != currentValue.and(0b1000_0000u))

        // Clear bit 7:
        val preRotate = currentValue.and(0b0111_1111u)
        // Assertion is to make sure we don't overflow:
        assert(preRotate < 0b1000_0000u)
        // Then a leftshift is just a multiplication by 2.
        val rotated = preRotate * 2u
        val finalValue =
            if (previousCarryFlag) {
                // "The previous value of the carry flag are copied to bit 0 of the register"
                rotated.xor(0b0000_0001u)
            } else {
                rotated
            }
        // Again, an assertion, to make sure there was no overflow:
        assert(finalValue <= 0xFFu)
        setValue(r, finalValue.toUByte())

        if (0u == finalValue) {
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