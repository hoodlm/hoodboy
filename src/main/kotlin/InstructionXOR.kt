import kotlin.reflect.KMutableProperty

interface InstructionXor: Instruction {
    override val size: UShort
        get() = 1u

    fun inputRegisters(registers: Registers): List<KMutableProperty<UByte>>
    fun resultRegister(registers: Registers): KMutableProperty<UByte>

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        val inputRegisters = inputRegisters(registers)
        assert(immediateData.size == 4)
        assert(inputRegisters.size == 2)
        val x = inputRegisters[0].getter.call()
        val y = inputRegisters[1].getter.call()
        val result = x.xor(y)
        resultRegister(registers).setter.call(result)
    }
}

class InstructionXorA(): InstructionXor {
    override fun inputRegisters(registers: Registers): List<KMutableProperty<UByte>> {
        return listOf(registers::A, registers::A)
    }

    override fun resultRegister(registers: Registers): KMutableProperty<UByte> = registers::A
}