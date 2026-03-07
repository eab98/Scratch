package scratch.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import scratch.model.*;

import java.util.List;

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

    private final IntegerProperty amount = new SimpleIntegerProperty(0);

    private Error errorAmount = new Error();

    public IntegerProperty getIndexProperty(){
        return index;
    }
    public IntegerProperty getCatalogIndexProperty(){
        return catalogIndex;
    }

    public IntegerProperty getAmountProperty() { return amount; }

    public ScratchViewModel(Program program) {
        this.program = program;
    }

    public ObservableList<Instruction> getCatalogue() {
        return Program.getCatalogue();
    }

    public ObservableList<Instruction> getInstru() {
        return Program.getInstructions();
    }

    public String getErrorAmount(){ return errorAmount.getMessage(); }

    public DoubleProperty coordXProperty() { return coordX; }

    public DoubleProperty coordYProperty() { return coordY; }

    public BooleanBinding canUp() {
        return index.greaterThan(0);
    }

    public BooleanBinding canDown() {
        return Bindings.createBooleanBinding(() -> {
            int idx = index.get();
            return idx >= 0 && idx < instructions.size() - 1;
        }, getInstru(), index);
    }

    public BooleanBinding canEmpty() {
        return Bindings.createBooleanBinding(() -> {
            List<Instruction> instr = getInstru();
            return !instr.isEmpty();
        }, getInstru());
    }

    public BooleanBinding canDuplicateOrDelete() {
        return index.greaterThan(-1);
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

    public void execute() {
        int nextIndex = indexExec.get() + 1;

        if (nextIndex < instructions.size()) {
            indexExec.set(nextIndex); // On passe à l'instruction suivante
            Instruction instru = instructions.get(nextIndex);

            // Position de départ
            Coordinates initial = new Coordinates(program.getPen().getCoord().getX(), program.getPen().getCoord().getY());

            // le modèle exécute l'action
            program.execute(instru);

            // Position d'arrivée
            Coordinates end = program.getPen().getCoord();
            coordX.set(end.getX());
            coordY.set(end.getY());

            // Si le stylo est baissé, on mémorise le trait à dessiner
            if (program.isPenDown()) {
                shape.addLine(new Line(initial, end));
            }
        }
    }

    public boolean isPenDown() {
        return program.isPenDown();
    }

    public void resetExecution() {
        indexExec.set(-1); // remet le compteur à zéro
        program.getPen().setCoord(new Coordinates(195, 195)); // Retour au centre
        program.getPen().setOrientation(-90); // Regarde vers le haut
        program.setPenDown(true);

        // màj sur la Vue
        coordX.set(195);
        coordY.set(195);
    }

    public int getOrientation() {
        return program.getPen().getOrientation();
    }

    public String getNameInstruParIndex() {
        int index = getIndexProperty().get();
        return instructions.get(index).getName();
    }

    public void changeAmount() {
        int index = getIndexProperty().get();
        Instruction instru = instructions.get(index);

        try {
            program.changeAmount(instru, amount.get());
        } catch(RuntimeException e) {
            errorAmount = new Error(e.getMessage());
        }
    }

    public int getParameterInstru() {
        int index = getIndexProperty().get();
        Instruction instru = instructions.get(index);

        return instru.getParameter();
    }

}
