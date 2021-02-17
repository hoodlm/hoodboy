import java.io.File

const val DEFAULT_PATH = "/home/hoodlm/Downloads/DMG_ROM.bin"

fun main(args: Array<String>) {
    val path =
        if (args.isEmpty()) {
            println("No args were provided; using default path $DEFAULT_PATH")
            DEFAULT_PATH
        } else {
            args[0]
        }
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
    val cpu = CPU(Registers(), memory, GameBoyInstructionInterpreter(), continueOnFatals = false)

    while(true) {
        cpu.fetchAndRunNextInstruction()
    }
}