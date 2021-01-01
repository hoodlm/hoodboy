fun UShort.splitHighAndLowBits(): Pair<UByte, UByte> {
    val lowBits: UByte = this.rem(Registers.BITSHIFT_8).toUByte()
    val highBits: UByte = ((this - lowBits) / Registers.BITSHIFT_8).toUByte()

    return Pair(highBits, lowBits)
}

fun Pair<UByte, UByte>.toUShort(): UShort {
    val highBits = this.first.toUShort()
    val lowBits = this.second.toUShort()

    return (lowBits + (highBits * Registers.BITSHIFT_8)).toUShort()
}