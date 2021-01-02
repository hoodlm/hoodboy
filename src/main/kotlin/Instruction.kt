enum class Instruction: (Registers, Memory, Pair<UByte, UByte>) -> Unit {
    NOOP {
        override fun invoke(r: Registers, m: Memory, immediateData: Pair<UByte, UByte>) {
            // NOOP
        }
    }
}