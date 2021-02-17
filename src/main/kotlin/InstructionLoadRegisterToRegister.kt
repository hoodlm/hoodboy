import kotlin.reflect.KMutableProperty

interface InstructionLoadValueToRegister: Instruction {
    override val size: UShort
        get() = 1u

    fun destRegister(registers: Registers): KMutableProperty<UByte>
    fun valueToLoad(registers: Registers, memory: Memory): UByte

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        destRegister(registers).setter.call(valueToLoad(registers, memory))
    }
}

class InstructionLoadRegisterToRegister(
    private val sourceRegister: (Registers) -> KMutableProperty<UByte>,
    private val destRegister: (Registers) -> KMutableProperty<UByte>
): InstructionLoadValueToRegister {
    override fun destRegister(registers: Registers): KMutableProperty<UByte> {
        return destRegister.invoke(registers)
    }

    override fun valueToLoad(registers: Registers, memory: Memory): UByte {
        return sourceRegister.invoke(registers).getter.call()
    }
}