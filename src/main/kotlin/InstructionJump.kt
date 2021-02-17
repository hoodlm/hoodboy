// NEXT TIME: NEED TO READ IMMEDIATE DATA AS SIGNED 8 BIT
interface InstructionJump: Instruction {
    override val size: UShort
        get() = 2u

    fun shouldJump(registers: Registers): Boolean

    override fun invoke(registers: Registers, memory: Memory, immediateData: List<UByte>) {
        assert(immediateData.size == 4)
        if (shouldJump(registers)) {
            val jumpDistance = immediateData[1].toByte()
            val jumpTo = registers.PC().toShort() + jumpDistance.toShort()
            registers.setPC(jumpTo.toUShort())
        }
    }
}

class InstructionJumpUnconditional: InstructionJump {
    override fun shouldJump(registers: Registers): Boolean = true
}

class InstructionJumpNotZ: InstructionJump {
    override fun shouldJump(registers: Registers): Boolean = !registers.flagZ()
}

class InstructionJumpZ: InstructionJump {
    override fun shouldJump(registers: Registers): Boolean = registers.flagZ()
}

class InstructionJumpNotC: InstructionJump {
    override fun shouldJump(registers: Registers): Boolean = !registers.flagC()
}

class InstructionJumpC: InstructionJump {
    override fun shouldJump(registers: Registers): Boolean = registers.flagC()
}