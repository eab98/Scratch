package scratch.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Program {
    private final Character pen;
    private final List<Instruction> instructions = new ArrayList<>();

    public Program(Character pen) {
        this.pen = pen;
    }

    public Character getPen() {
        return pen;
    }
}
