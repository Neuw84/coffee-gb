package eu.rekawek.coffeegb.cpu;

import eu.rekawek.coffeegb.AddressSpace;

public class Command {

    private final int opcode;

    private final int cycles;

    private final int argsLength;

    private final String label;

    private final Operation operation;

    public Command(int opcode, int cycles, int argsLength, String label, Operation operation) {
        this.opcode = opcode;
        this.cycles = cycles;
        this.argsLength = argsLength;
        this.label = label;
        this.operation = operation;
    }

    interface Operation {
        void run(Registers registers, AddressSpace addressSpace, int[] arguments);
    }
}