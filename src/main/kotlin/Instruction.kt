enum class Instruction: () -> Unit {
    NOOP {
        override fun invoke() {
            // NOOP
        }
    }
}