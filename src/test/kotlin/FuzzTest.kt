import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUBytes

class FuzzTest {
    @Test
    fun randomInstructions() {
        val runCount = 500000
        val seed = 20210105
        val r = Random(seed)

        val registers = Registers()
        val memory = Memory()
        val cpu = CPU(registers, memory, GameBoyInstructionInterpreter())

        for (i in 0..runCount) {
            val bytes = r.nextUBytes(4)
            try {
                cpu.execInstruction(Pair(bytes[0], bytes[1]), listOf(bytes[2], bytes[3]))
            } catch (x: RuntimeException) {
                if (true == x.message?.endsWith("is not yet implemented")) {
                    // ignore unimplemented instructions
                } else {
                    throw x
                }
            }
        }
    }
}