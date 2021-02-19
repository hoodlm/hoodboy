import kotlin.reflect.KMutableProperty

class InstructionCallLiteralAddress: Instruction {
    override val size: UShort = 3u

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
        val previousStackPointer = registers.SP()
        val currentAddressBytes = registers.PC().splitHighAndLowBits()
        memory.putByte(previousStackPointer.dec(), currentAddressBytes.first)
        memory.putByte(previousStackPointer.dec().dec(), currentAddressBytes.second)

        val newAddress = Pair(immediateData[2], immediateData[1]).toUShort()
        registers.setPC(newAddress)
        registers.setSP(previousStackPointer.dec().dec())
    }
}

class InstructionReturn: Instruction {
    override val size: UShort = 1u

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(4 == immediateData.size)
        val previousStackPointer = registers.SP()
        val returnAddress = Pair(
            memory.getByte(previousStackPointer.inc()),
            memory.getByte(previousStackPointer)).toUShort()
        registers.setPC(returnAddress)
        registers.setSP(previousStackPointer.inc().inc())
    }
}

interface InstructionPushToStack: Instruction {
    override val size: UShort
        get() = 1u

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        val previousStackPointer = registers.SP()
        val bytesToPush = valueToPush(registers).splitHighAndLowBits()
        memory.putByte(previousStackPointer.dec(), bytesToPush.first)
        memory.putByte(previousStackPointer.dec().dec(), bytesToPush.second)
        registers.setSP(previousStackPointer.dec().dec())
    }

    fun valueToPush(r: Registers): UShort
}

class InstructionPushBCToStack: InstructionPushToStack {
    override fun valueToPush(r: Registers): UShort = r.BC()
}

class InstructionPushDEToStack: InstructionPushToStack {
    override fun valueToPush(r: Registers): UShort = r.DE()
}

class InstructionPushHLToStack: InstructionPushToStack {
    override fun valueToPush(r: Registers): UShort = r.HL()
}

class InstructionPushAFToStack: InstructionPushToStack {
    override fun valueToPush(r: Registers): UShort = r.AF()
}

interface InstructionPopFromStack: Instruction {
    override val size: UShort
        get() = 1u

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        val previousStackPointer = registers.SP()
        val poppedData = Pair(
            memory.getByte(previousStackPointer.inc()),
            memory.getByte(previousStackPointer)
        ).toUShort()
        setInDestRegister(registers, poppedData)
        registers.setSP(previousStackPointer.inc().inc())
    }

    fun setInDestRegister(r: Registers, value: UShort)
}

class InstructionPopBCFromStack: InstructionPopFromStack {
    override fun setInDestRegister(r: Registers, value: UShort) = r.setBC(value)
}

class InstructionPopDEFromStack: InstructionPopFromStack {
    override fun setInDestRegister(r: Registers, value: UShort) = r.setDE(value)
}

class InstructionPopHLFromStack: InstructionPopFromStack {
    override fun setInDestRegister(r: Registers, value: UShort) = r.setHL(value)
}

class InstructionPopAFFromStack: InstructionPopFromStack {
    override fun setInDestRegister(r: Registers, value: UShort) = r.setAF(value)
}