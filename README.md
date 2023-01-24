This is an incomplete gameboy emulator in Kotlin.

As of writing this README, I hadn't touched it in 2 years. These are the tabs
I had open in the VM that I was using to work on it:

* https://meganesulli.com/generate-gb-opcodes/
* https://gbdev.gg8.se/wiki/articles/Gameboy_Bootstrap_ROM
* https://cturt.github.io/cinoop.html
* http://marc.rawer.de/Gameboy/Docs/GBCPUman.pdf
* https://github.com/CTurt/Cinoop/blob/master/source/cpu.c

It looks like I made decent progress on implementing the instruction set.
No work on graphics or sound. From what I recall, instructions don't
implement NumberOfCycles (meaning there would probably be timing bugs if I
didn't go back and backfill them).
