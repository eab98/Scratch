package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Program {
    private final Character pen;
    private static final ObservableList<Instruction> catalogue = FXCollections.observableArrayList(
            new Translation("Avancer de", MovementType.FORWARD, 0, 0),
            new Rotation("Tourner à gauche de", MovementType.LEFT,  0),
            new Rotation("Tourner à droite de", MovementType.RIGHT,  0),
            new PenAction("Lever stylo", PenMovement.PEN_UP),
            new PenAction("Abaisser stylo", PenMovement.PEN_DOWN)
            );

    private static final ObservableList<Instruction> instructions = FXCollections.observableArrayList();
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

    public void addInstruction(int idx) {

        Instruction instru = catalogue.get(idx);
        instructions.add(instru.newInstruction());
    }

    public void up(int idx) {
        if(idx > 0 && idx < instructions.size()) {
            Instruction instru = instructions.set(idx, instructions.get(idx - 1));
            instructions.set(idx-1, instru);
        }
    }

    public void down(int idx) {
        if(idx>=0 && idx < instructions.size() - 1) {
            Instruction instru = instructions.set(idx, instructions.get(idx + 1));
            instructions.set(idx + 1, instru);
        }
    }

    public void deleteInstruction(int index) {
        instructions.remove(index);

    }

    public void duplicate(int idx){
        if(idx >= 0 && idx < instructions.size()) {
            Instruction instru = instructions.get(idx);
            instructions.add(instru.duplicate());
        }
    }

    public void execute(Instruction instr) {
        if (instr instanceof PenAction)
            setPenDown(((PenAction) instr).changePenStatus(this.penDown));
        else
            instr.execute(getPen());
    }

    public void empty(){
        instructions.clear();
    }



//    public ObservableList<Instruction> getCatalogue() {
//        return catalogue;
//    }

    public static ObservableList<Instruction> getCatalogue() {
        return FXCollections.unmodifiableObservableList(catalogue);
    }

    public static ObservableList<Instruction> getInstructions() {
        return FXCollections.unmodifiableObservableList(instructions);
    }
}
