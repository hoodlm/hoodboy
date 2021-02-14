val BITSHIFT_8: UShort = 0x0100u;
val HALF_CARRY_MASK: UByte = 0b0001_0000u

val ONE_BIT_MASKS: List<UByte> = listOf(
    0b0000_0001u, // bit 0
    0b0000_0010u,
    0b0000_0100u,
    0b0000_1000u,
    0b0001_0000u,
    0b0010_0000u,
    0b0100_0000u,
    0b1000_0000u  // bit 7
)

fun UShort.splitHighAndLowBits(): Pair<UByte, UByte> {
    val lowBits: UByte = this.rem(BITSHIFT_8).toUByte()
    val highBits: UByte = ((this - lowBits) / BITSHIFT_8).toUByte()

    return Pair(highBits, lowBits)
}

fun Pair<UByte, UByte>.toUShort(): UShort {
    val highBits = this.first.toUShort()
    val lowBits = this.second.toUShort()

    return (lowBits + (highBits * BITSHIFT_8)).toUShort()
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

// "H=1 if and only if the upper nibble had to change as a result of the operation on the lower nibble"
fun wasHalfCarried(before: UByte, after: UByte): Boolean {
    // e.g.
    //       0001_1110
    //  XOR  0000_1111
    //    -> 0001_0001
    //  AND  0001_0000
    //    -> 0001_0000
    // vs.
    //       0001_1110
    //  XOR  0001_1111
    //    -> 0000_0001
    //  AND  0001_0000
    //    -> 0000_0000
    return before.xor(after).and(HALF_CARRY_MASK).equals(HALF_CARRY_MASK)
}

fun wasHalfCarried(before: UShort, after: UShort): Boolean {
    // take last 8 bytes and call UByte version:
    return wasHalfCarried(
        before.and(0x00FFu).toUByte(),
        after.and(0x00FFu).toUByte()
    )
}