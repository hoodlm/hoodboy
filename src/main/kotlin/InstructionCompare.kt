interface InstructionCompare: Instruction {
    override val size: UShort
        get() = 1u

    fun nToCompare(r: Registers, m: Memory, d: List<UByte>): UByte

    override fun invoke(r: Registers, m: Memory, data: List<UByte>) {
        assert(data.size == 4)
        val n = nToCompare(r, m, data)
        val diff = r.A - n
        r.setFlagZ(r.A == n)
        r.setFlagC(r.A < n)
        r.setFlagH(wasHalfCarried(r.A, diff.toUByte()))
        r.setFlagN(true)
    }
}

class InstructionCompareToLiteral: InstructionCompare {
    override val size: UShort
        get() = 2u

    override fun nToCompare(r: Registers, m: Memory, d: List<UByte>): UByte {
        return d[1]
    }
}

class InstructionCompareToA: InstructionCompare {
    override fun nToCompare(r: Registers, m: Memory, d: List<UByte>): UByte {
        return r.A
    }
}

class InstructionCompareToB: InstructionCompare {
    override fun nToCompare(r: Registers, m: Memory, d: List<UByte>): UByte {
        return r.B
    }
}

class InstructionCompareToC: InstructionCompare {
    override fun nToCompare(r: Registers, m: Memory, d: List<UByte>): UByte {
        return r.C
    }
}

class InstructionCompareToD: InstructionCompare {
    override fun nToCompare(r: Registers, m: Memory, d: List<UByte>): UByte {
        return r.D
    }
}

class InstructionCompareToE: InstructionCompare {
    override fun nToCompare(r: Registers, m: Memory, d: List<UByte>): UByte {
        return r.E
    }
}

class InstructionCompareToH: InstructionCompare {
    override fun nToCompare(r: Registers, m: Memory, d: List<UByte>): UByte {
        return r.H
    }
}

class InstructionCompareToL: InstructionCompare {
    override fun nToCompare(r: Registers, m: Memory, d: List<UByte>): UByte {
        return r.L
    }
}