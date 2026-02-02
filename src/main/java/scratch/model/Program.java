package scratch.model;

import java.util.HashSet;
import java.util.Set;

public class Program {
    private final Character pen;
    private final Set<Instruction> instructions = new HashSet<>();

    public Program(Character pen) {
        this.pen = pen;
    }

    public Character getPen() {
        return pen;
    }
}
