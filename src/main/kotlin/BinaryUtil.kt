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

fun UByte.toHexString(): String {
    return "0x%02x".format(this.toInt())
}

fun UShort.toHexString(): String {
    return "0x%04x".format(this.toInt())
}

fun Pair<UByte, UByte>.toHexString(): String {
    return this.toUShort().toHexString()
}

fun List<UByte>.toHexString(): String {
    return "0x" + this.map {
        "%02x".format(it.toInt())
    }.joinToString("")
}