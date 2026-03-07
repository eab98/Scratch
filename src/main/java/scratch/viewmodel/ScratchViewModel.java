package scratch.viewmodel;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import scratch.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScratchViewModel {
    private final Program program;

    private final Shape shape = new Shape();

    private List<Instruction> instructions = getInstru();

    private final IntegerProperty index = new SimpleIntegerProperty(-1),
            catalogIndex = new SimpleIntegerProperty(-1);
    //indice de l'instruction en exécution
    private final IntegerProperty indexExec = new SimpleIntegerProperty(-1);

    private final DoubleProperty coordX = new SimpleDoubleProperty(0);
    private final DoubleProperty coordY = new SimpleDoubleProperty(0);

    public IntegerProperty getIndexProperty(){
        return index;
    }
    public IntegerProperty getCatalogIndexProperty(){
        return catalogIndex;
    }
    public IntegerProperty getIndexExec() { return indexExec; }

    public ScratchViewModel(Program program) {
        this.program = program;
    }

    public ObservableList<Instruction> getCatalogue() {
        return Program.getCatalogue();
    }

    public ObservableList<Instruction> getInstru() {
        return Program.getInstructions();
    }

    public DoubleProperty coordXProperty() { return coordX; }

    public DoubleProperty coordYProperty() { return coordY; }

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

    public void execute() {
        Instruction instru = instructions.get(indexExec.get());
        Coordinates initial = program.getPen().getCoord();
        program.execute(instru);
        Coordinates end = program.getPen().getCoord();
        Line line = new Line(initial, end);
        shape.addLine(line);
    }

}
