package scratch.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Program {
    private final Character pen;
    private final List<Instruction> instructions = new ArrayList<>();
    //private int instructionIndex = 0;
    private boolean penDown = true;   //true si PEN_DOWN, false si PEN_UP

    public Program(Character pen) {
        this.pen = pen;
    }

    public Character getPen() {
        return pen;
    }

    public boolean isPenDown() {
        return penDown;
    }

    public void setPenDown(boolean penDown) {
        this.penDown = penDown;
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
            if (instr instanceof PenAction)
                setPenDown(((PenAction) instr).changePenStatus(this.penDown));
            else
                instr.execute(getPen());
        }
    }
}
