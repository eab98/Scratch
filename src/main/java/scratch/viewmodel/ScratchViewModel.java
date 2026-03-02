package scratch.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import scratch.model.Instruction;
import scratch.model.Program;

import java.util.ArrayList;
import java.util.List;

public class ScratchViewModel {
    private final Program program;

    private List<Instruction> instructions = getInstru();

    private final IntegerProperty index = new SimpleIntegerProperty(-1),
            catalogIndex = new SimpleIntegerProperty(-1);

    public ScratchViewModel(Program program) {
        this.program = program;
    }

    public ObservableList<Instruction> getPossibleInstru() {
        return Program.getCatalogue();
    }

    public ObservableList<Instruction> getInstru() {
        return Program.getInstructions();
    }

    public void addInstruction(Instruction instru) {
        program.addInstruction(instru);
    }

    public void up() {
        int idx = index.get();
        if (idx > 0 && idx < getInstru().size()) {
            program.up(idx);
            index.setValue(idx - 1);
        }
    }
    public void down() {
        int idx = index.get();
        if(idx >= 0 && idx < getInstru().size() - 1 ) {
            program.down(idx);
            index.setValue(idx + 1);
        }
    }

    public void removeInstru(){
        int idx = index.get();
        if (idx >= 0 && idx < getInstru().size()){
            program.deleteInstruction(idx);
        }
    }

    public void duplicate() {
        int idx = index.get();
        if(idx >= 0 && idx < getInstru().size()){
            program.duplicate(idx);

        }
    }

}
