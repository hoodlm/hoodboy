class Registers {
    companion object {
        private const val Z_MASK: UByte = 0b1000_0000u
        private const val N_MASK: UByte = 0b0100_0000u
        private const val H_MASK: UByte = 0b0010_0000u
        private const val C_MASK: UByte = 0b0001_0000u
    }

    var A: UByte = 0u;
    var B: UByte = 0u;
    var C: UByte = 0u;
    var D: UByte = 0u;
    var E: UByte = 0u;
    var F: UByte = 0u;
    var H: UByte = 0u;
    var L: UByte = 0u;
    private var SP: UShort = 0u;
    private var PC: UShort = 0u;

    fun setAF(af: UShort) {
        af.splitHighAndLowBits().also {
            this.A = it.first
            this.F = it.second
        }
    }

    fun AF(): UShort {
        return Pair(this.A, this.F).toUShort()
    }

    fun setBC(af: UShort) {
        af.splitHighAndLowBits().also {
            this.B = it.first
            this.C = it.second
        }
    }

    fun BC(): UShort {
        return Pair(this.B, this.C).toUShort()
    }

    fun setDE(af: UShort) {
        af.splitHighAndLowBits().also {
            this.D = it.first
            this.E = it.second
        }
    }

    fun DE(): UShort {
        return Pair(this.D, this.E).toUShort()
    }

    fun setHL(af: UShort) {
        af.splitHighAndLowBits().also {
            this.H = it.first
            this.L = it.second
        }
    }

    fun HL(): UShort {
        return Pair(this.H, this.L).toUShort()
    }

    fun setSP(sp: UShort) {
        this.SP = sp
    }

    fun SP(): UShort {
        return SP
    }

    fun setPC(pc: UShort) {
        this.PC = pc
    }

    fun PC(): UShort {
        return PC
    }

    /* flag accessors/setters, in register F
     * 7  6  5  4  3  2  1  0
     * ----------------------
     * Z  N  H  C  0  0  0  0
     */
    fun flagZ(): Boolean {
        return checkFlag(Z_MASK)
    }

    fun flagN(): Boolean {
        return checkFlag(N_MASK)
    }

    fun flagH(): Boolean {
        return checkFlag(H_MASK)
    }

    fun flagC(): Boolean {
        return checkFlag(C_MASK)
    }

    private fun checkFlag(mask: UByte): Boolean {
        return F.and(mask).equals(mask)
    }

    fun setFlagZ(on: Boolean) {
        setFlag(on, Z_MASK)
    }

    fun setFlagN(on: Boolean) {
        setFlag(on, N_MASK)
    }

    fun setFlagH(on: Boolean) {
        setFlag(on, H_MASK)
    }

    fun setFlagC(on: Boolean) {
        setFlag(on, C_MASK)
    }

    private fun setFlag(on: Boolean, mask: UByte) {
        if (on) {
            F = F.or(mask)
        } else {
            F = F.and(mask.inv())
        }
    }

    fun clear() {
        A  = 0u;
        B  = 0u;
        C  = 0u;
        D  = 0u;
        E  = 0u;
        F  = 0u;
        H  = 0u;
        L  = 0u;
        SP = 0u;
        PC = 0u;
    }

    fun dumpRegisters(): String {
        return "A=${A.toHexString()}, F=${F.toHexString()}, B=${B.toHexString()}, " +
            "C=${C.toHexString()}, D=${D.toHexString()}, E=${E.toHexString()}, " +
            "H=${H.toHexString()}, L=${L.toHexString()}, SP=${SP.toHexString()}, PC=${PC.toHexString()}; " +
            "flags: C=${flagC()}, H=${flagH()}, Z=${flagZ()}, N=${flagN()}"
    }
}