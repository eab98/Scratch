package scratch.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Program {
    private final Character pen;
    private final List<Instruction> instructions = new ArrayList<>();
    //private int instructionIndex = 0;

    public Program(Character pen) {
        this.pen = pen;
    }

    public Character getPen() {
        return pen;
    }

    public void addInstruction(Instruction instr) {
        instructions.add(instr);
        //++instructionIndex;
    }

    public void deleteInstruction(int index) {
        instructions.remove(index);
        /*if (instructionIndex >= instructions.size())
            instructionIndex--;*/
    }

    public void execute(int instructionIndex) {
        if (instructionIndex < instructions.size()) {
            Instruction instr = instructions.get(instructionIndex);
            instr.execute(getPen());
        } else {
            throw new RuntimeException("Index out of bounds");
        }
    }
}
