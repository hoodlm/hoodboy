class Memory(val size: Int = 0x10000) {
    private val bus: UByteArray = UByteArray(size)

    fun getByte(address: UShort): UByte {
        return bus.get(address.toInt())
    }

    fun putByte(address: UShort, value: UByte) {
        bus.set(address.toInt(), value);
    }
}