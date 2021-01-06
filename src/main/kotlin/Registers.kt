class Registers {
    companion object {
        const val BITSHIFT_8: UShort = 0x0100u;
    }

    var A: UByte = 0u;
    var B: UByte = 0u;
    var C: UByte = 0u;
    var D: UByte = 0u;
    var E: UByte = 0u;
    var F: UByte = 0u;
    var H: UByte = 0u;
    var L: UByte = 0u;
    var SP: UShort = 0u;
    var PC: UShort = 0u;

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
        return "A=$A, F=$F, B=$B, C=$C, D=$D, E=$E, H=$H, L=$L, SP=$SP, PC=$PC"
    }
}