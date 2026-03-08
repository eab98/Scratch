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

    private final StringProperty errorMessage = new SimpleStringProperty("");

    // état pour les boutons charger et executer
    private boolean isLoaded = false;
    private boolean isRunning = false;

    private final StringProperty leftBtnText = new SimpleStringProperty("Charger");
    private final BooleanProperty leftBtnDisabled = new SimpleBooleanProperty(true);

    private final StringProperty rightBtnText = new SimpleStringProperty("Exécuter");
    private final BooleanProperty rightBtnDisabled = new SimpleBooleanProperty(true);

    public StringProperty leftBtnTextProperty() { return leftBtnText; }
    public BooleanProperty leftBtnDisabledProperty() { return leftBtnDisabled; }
    public StringProperty rightBtnTextProperty() { return rightBtnText; }
    public BooleanProperty rightBtnDisabledProperty() { return rightBtnDisabled; }

    public IntegerProperty getIndexProperty(){
        return index;
    }
    public IntegerProperty getCatalogIndexProperty(){
        return catalogIndex;
    }

    public IntegerProperty getAmountProperty() { return amount; }

    public ScratchViewModel(Program program) {
        this.program = program;
        updateState();
    }

    public ObservableList<Instruction> getCatalogue() {
        return Program.getCatalogue();
    }

    public ObservableList<Instruction> getInstru() {
        return Program.getInstructions();
    }

    public StringProperty getErrorMessage() {return errorMessage;}

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
        notifyProgramChanged();
    }

    public void up() {
        int idx = index.get();
        if (idx > 0 && idx < getInstru().size()) {
            program.up(idx);
            index.setValue(idx - 1);
        }
        notifyProgramChanged();
    }
    public void down() {
        int idx = index.get();
        if(idx >= 0 && idx < getInstru().size() - 1 ) {
            program.down(idx);
            index.setValue(idx + 1);
        }
        notifyProgramChanged();
    }

    public void removeInstru(){
        int idx = index.get();
        if (idx >= 0 && idx < getInstru().size()){
            program.deleteInstruction(idx);
        }
        notifyProgramChanged();
    }

    public void duplicate() {
        int idx = index.get();
        if(idx >= 0 && idx < getInstru().size()){
            program.duplicate(idx);
        }
        notifyProgramChanged();
    }

    public void empty() {
        program.empty();
        notifyProgramChanged();
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

    public void updateState() {
        if (getInstru().isEmpty()) {
            // état 1 (quand on lance le programme) : charger et executer desact
            isLoaded = false;
            isRunning = false;
            leftBtnText.set("Charger");
            leftBtnDisabled.set(true);
            rightBtnText.set("Exécuter");
            rightBtnDisabled.set(true);
        } else if (!isLoaded) {
            // état 2 : charger actif et exec desact
            leftBtnText.set("Charger");
            leftBtnDisabled.set(false);
            rightBtnText.set("Exécuter");
            rightBtnDisabled.set(true);
        } else if (isLoaded && !isRunning) {
            // état 3 : charger devient ré-initialiser et executer s'active
            leftBtnText.set("Ré-initialiser");
            leftBtnDisabled.set(false);
            rightBtnText.set("Exécuter");
            rightBtnDisabled.set(false);
        } else if (isRunning) {
            // état 4 : gestion bouton suivant et executer
            leftBtnText.set("Ré-initialiser");
            leftBtnDisabled.set(false);

            // on a tout terminé ?
            if (indexExec.get() >= getInstru().size() - 1) {
                rightBtnText.set("Exécuter");
                rightBtnDisabled.set(true);
            } else {
                rightBtnText.set("Suivant");
                rightBtnDisabled.set(false);
            }
        }
    }

    public void clickLeftButton() {
        if (!isLoaded) {
            isLoaded = true; // qund on clic sur chargé
        } else {
            // On clic sur reset
            isRunning = false;
            resetExecution();
        }
        updateState(); // update le visuel
    }

    public void clickRightButton() {
        if (!isRunning) {
            isRunning = true; // bouton exec qui devient suivant
        } else {
            execute(); // on avance d'un step
        }
        updateState();
    }

    public void notifyProgramChanged() {
        // dès qu'on modifie dans programme, on annule et on doit recharger à nouveau
        isLoaded = false;
        isRunning = false;
        resetExecution();
        updateState();
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
            errorMessage.set("");
        } catch(RuntimeException e) {
            errorMessage.set(e.getMessage());
        }
    }

    public int getParameterInstru() {
        int index = getIndexProperty().get();
        Instruction instru = instructions.get(index);

        return instru.getParameter();
    }

    public boolean isPenAction() {
        int index = getIndexProperty().get();
        Instruction instru = instructions.get(index);

        return instru.getType() == InstructionType.PEN_UP || instru.getType() == InstructionType.PEN_DOWN;
    }

}
