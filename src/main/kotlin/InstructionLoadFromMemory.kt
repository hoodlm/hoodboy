import kotlin.reflect.KMutableProperty

interface InstructionLoadFromMemoryToRegister: Instruction {
    override val size: UShort
        get() = 1u
    fun sourceAddress(registers: Registers, data: List<UByte>): UShort
    fun destinationRegister(registers: Registers): KMutableProperty<UByte>
    fun postInvoke(registers: Registers) {
        // Optional post-invoke code hook, e.g. for decrement/increment register instructions like LD A (HL+)
    }

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(immediateData.size == 4)
        val sourceAddress = sourceAddress(registers, immediateData)
        val value = memory.getByte(sourceAddress)
        destinationRegister(registers).setter.call(value)
        postInvoke(registers)
    }
}

interface InstructionLoadFromHLAddress: InstructionLoadFromMemoryToRegister {
    override fun sourceAddress(registers: Registers, data: List<UByte>) = registers.HL()
}

open class InstructionLoadFromHLAddressToA: InstructionLoadFromHLAddress {
    override fun destinationRegister(registers: Registers): KMutableProperty<UByte> = registers::A
}

class InstructionLoadFromHLAddressToAIncrement: InstructionLoadFromHLAddressToA() {
    override fun postInvoke(registers: Registers) {
        registers.setHL(registers.HL().inc())
    }
}

class InstructionLoadFromHLAddressToADecrement: InstructionLoadFromHLAddressToA() {
    override fun postInvoke(registers: Registers) {
        registers.setHL(registers.HL().dec())
    }
}

interface InstructionLoadFromBCAddress: InstructionLoadFromMemoryToRegister {
    override fun sourceAddress(registers: Registers, data: List<UByte>) = registers.BC()
}

open class InstructionLoadFromBCAddressToA: InstructionLoadFromBCAddress {
    override fun destinationRegister(registers: Registers): KMutableProperty<UByte> = registers::A
}

interface InstructionLoadFromDEAddress: InstructionLoadFromMemoryToRegister {
    override fun sourceAddress(registers: Registers, data: List<UByte>) = registers.DE()
}

open class InstructionLoadFromDEAddressToA: InstructionLoadFromDEAddress {
    override fun destinationRegister(registers: Registers): KMutableProperty<UByte> = registers::A
}