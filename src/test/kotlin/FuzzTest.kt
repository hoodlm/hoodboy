import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUBytes

class FuzzTest {
    @Test
    fun randomInstructions() {
        var extendedInstructionBias = 0.10 // 10% of the time, choose an extended instruction
        var successCount = 0
        val runCount = 500000
        val seed = 20210219
        val r = Random(seed)

        val registers = Registers()
        val memory = Memory()
        val cpu = CPU(registers, memory, GameBoyInstructionInterpreter(), verbose = false)

        for (i in 0..runCount) {
            val bytes = r.nextUBytes(4)
            if (r.nextFloat() < extendedInstructionBias) {
                bytes[0] = 0xCBu
            }
            try {
                cpu.execInstruction(listOf(bytes[0], bytes[1], bytes[2], bytes[3]))
                successCount += 1
            } catch (x: RuntimeException) {
                if (true == x.message?.endsWith("is not yet implemented")) {
                    // ignore unimplemented instructions
                } else {
                    throw x
                }
            }
        }
        println("${successCount}/${runCount} instructions were NOT skipped; estimated progress is ${100 * successCount/runCount}%")
    }
}