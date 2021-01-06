import java.io.File
import java.lang.RuntimeException

fun main(args: Array<String>) {
    println("HELLO")

    if (args.isEmpty()) {
        throw RuntimeException("No args were provided; expected a ROM filepath as arg 0")
    }
    val path = args[0]
    println("Loading ROM from $path")
    val rom = File(path).readBytes()
    println("Read ROM from $path; loading it into memory now")
    val memory = Memory()
    var bytesRead = 0
    rom.forEachIndexed() { index, byte ->
        memory.putByte(index.toUShort(), byte.toUByte())
        bytesRead++
    }

    println("Successfully read ROM ($bytesRead bytes) into memory; starting up the CPU now!")
    val cpu = CPU(Registers(), memory, GameBoyInstructionInterpreter(), continueOnFatals = true)

    while(true) {
        cpu.fetchAndRunNextInstruction()
    }
}