import kotlin.reflect.KMutableProperty

interface InstructionLoadFromRegisterToMemory: Instruction {
    override val size: UShort
        get() = 1u
    fun destinationAddressRegister(registers: Registers): () -> UShort
    fun valueRegister(registers: Registers): KMutableProperty<UByte>
    fun postInvoke(registers: Registers) {
        // Optional post-invoke code hook, e.g. for decrement/increment register instructions like LD (HL-) A
    }

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(immediateData.size == 4)
        val destinationAddress = destinationAddressRegister(registers).invoke()
        val destinationValue = valueRegister(registers).getter.call()
        memory.putByte(destinationAddress, destinationValue)
        postInvoke(registers)
    }
}

class InstructionLoadAFromHLDecrement: InstructionLoadFromRegisterToMemory {
    override fun destinationAddressRegister(registers: Registers): () -> UShort = registers::HL
    override fun valueRegister(registers: Registers): KMutableProperty<UByte> = registers::A
    override fun postInvoke(registers: Registers) {
        registers.setHL(registers.HL().dec())
    }
}

class InstructionLoadAFromHLIncrement: InstructionLoadFromRegisterToMemory {
    override fun destinationAddressRegister(registers: Registers): () -> UShort = registers::HL
    override fun valueRegister(registers: Registers): KMutableProperty<UByte> = registers::A
    override fun postInvoke(registers: Registers) {
        registers.setHL(registers.HL().inc())
    }
}