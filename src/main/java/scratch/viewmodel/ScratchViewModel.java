package scratch.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import scratch.model.Instruction;
import scratch.model.Program;

import java.util.List;

public class ScratchViewModel {
    private final Program program;

    private List<Instruction> instructions = getInstru();

    private final IntegerProperty index = new SimpleIntegerProperty(-1),
            catalogIndex = new SimpleIntegerProperty(-1);

    public IntegerProperty getIndexProperty(){
        return index;
    }
    public IntegerProperty getCatalogIndexProperty(){
        return catalogIndex;
    }

    public ScratchViewModel(Program program) {
        this.program = program;
    }

    public ObservableList<Instruction> getCatalogue() {
        return Program.getCatalogue();
    }

    public ObservableList<Instruction> getInstru() {
        return Program.getInstructions();
    }

    public void addInstruction() {
        int idx = catalogIndex.get();
        if (idx >= 0 && idx < getCatalogue().size()) {
            program.addInstruction(idx);
        }

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

    public void empty() {
        program.empty();
    }

}
