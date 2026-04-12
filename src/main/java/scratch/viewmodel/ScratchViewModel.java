package scratch.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scratch.model.*;

import java.util.List;

public class ScratchViewModel {
    private final Program program;

    private List<Instruction> instructions = getInstru();

    private final IntegerProperty index = new SimpleIntegerProperty(-1),
            catalogIndex = new SimpleIntegerProperty(-1);
    //indice de l'instruction en exécution
    private final IntegerProperty indexExec = new SimpleIntegerProperty(-1);

    private final DoubleProperty coordX = new SimpleDoubleProperty(0);
    private final DoubleProperty coordY = new SimpleDoubleProperty(0);
    private final IntegerProperty direction = new SimpleIntegerProperty(0);

    private final StringProperty amount = new SimpleStringProperty("");

    private final StringProperty errorMessage = new SimpleStringProperty("");

    // état pour les boutons charger et executer
    private boolean isLoaded = false;
    private boolean isRunning = false;

    private Runnable onStepExecuted;

    private final StringProperty leftBtnText = new SimpleStringProperty("Charger");
    private final BooleanProperty leftBtnDisabled = new SimpleBooleanProperty(true);

    private final StringProperty rightBtnText = new SimpleStringProperty("Exécuter");
    private final BooleanProperty rightBtnDisabled = new SimpleBooleanProperty(true);

    public StringProperty leftBtnTextProperty() { return leftBtnText; }
    public BooleanProperty leftBtnDisabledProperty() { return leftBtnDisabled; }
    public StringProperty rightBtnTextProperty() { return rightBtnText; }
    public BooleanProperty rightBtnDisabledProperty() { return rightBtnDisabled; }
    private final ObservableList<Variable> variables = FXCollections.observableArrayList();

    public ObservableList<Variable> getVariables() {
        return variables;
    }

    public IntegerProperty getIndexProperty(){
        return index;
    }
    public IntegerProperty getCatalogIndexProperty(){
        return catalogIndex;
    }

    public StringProperty getAmountProperty() { return amount; }

    private final StringProperty turtleInfoText = new SimpleStringProperty("");
    public StringProperty turtleInfoTextProperty() { return turtleInfoText; }

    private final ExecutionViewModel executionVM = new ExecutionViewModel(this);
    public ExecutionViewModel getExecutionVM() { return executionVM; }

    public ScratchViewModel(Program program) {
        this.program = program;
        updateState();

        updateTurtleInfoText();

        coordX.addListener((obs, oldVal, newVal) -> updateTurtleInfoText());
        coordY.addListener((obs, oldVal, newVal) -> updateTurtleInfoText());
        direction.addListener((obs, oldVal, newVal) -> updateTurtleInfoText());
    }

    public ObservableList<Instruction> getCatalogue() {
        return Program.getCatalogue();
    }

    public ObservableList<Instruction> getInstru() {
        return Program.getInstructions();
    }

    public StringProperty getErrorMessage() {return errorMessage;}

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

    public BooleanBinding canDuplicate() {
        return Bindings.createBooleanBinding(() -> {
            int idx = index.get();
            if (idx > -1) {
                List<Instruction> instructions = getInstru();
                Instruction instr = instructions.get(idx);
                return instr.getType() != InstructionType.VAR_DECLARATION
                        && instr.getType() != InstructionType.VAR_ASSIGNEMENT;
            }
            return false;
        }, index, getInstru());
    }

    public BooleanBinding canDelete() {
        return index.greaterThan(-1);
    }

    public void addInstruction() {
        int idx = catalogIndex.get();
        int insertIndex = index.get();
        if (idx >= 0 && idx < getCatalogue().size()) {
            program.addInstruction(idx, insertIndex);

            notifyProgramChanged();

            index.set(insertIndex + 1);
        }
    }

    public void up() {
        int idx = index.get();
        if (idx > 0 && idx < getInstru().size()) {
            program.up(idx);
            notifyProgramChanged();
            index.set(idx - 1);
        }
    }
    public void down() {
        int idx = index.get();
        if(idx >= 0 && idx < getInstru().size() - 1 ) {
            program.down(idx);
            notifyProgramChanged();
            index.set(idx + 1);
        }
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
            if (instructions.get(nextIndex).getType() == InstructionType.END_REPEAT) {
                int loopStart = program.getLoopStart(nextIndex);
                if (!program.loopEnded(loopStart))
                    nextIndex = loopStart;
            }
            indexExec.set(nextIndex); // On passe à l'instruction suivante
            index.set(nextIndex); // Synchronise la sélection visuelle
            Instruction instru = instructions.get(nextIndex);

            // le modèle exécute l'action
            program.execute(instru);

            // Position et direction d'arrivée
            Coordinates end = program.getPen().getCoord();
            coordX.set(end.getX());
            coordY.set(end.getY());
            direction.set(program.getPen().getOrientation());

            if (onStepExecuted != null) {
                onStepExecuted.run();
            }
        }
    }

    public boolean isPenDown() {
        return program.isPenDown();
    }

    public void resetExecution() {
        indexExec.set(-1); // remet le compteur à zéro
        index.set(-1);
        program.reset();

        // màj sur la Vue
        coordX.set(program.getPen().getCoord().getX());
        coordY.set(program.getPen().getCoord().getY());
        direction.set(program.getPen().getOrientation());
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
        }else if(!program.checkProgramValidity()){
            leftBtnText.set("Charger");
            leftBtnDisabled.set(true);
            rightBtnText.set("Exécuter");
            rightBtnDisabled.set(true);
        }
        else if (!isLoaded) {
            // état 2 : charger actif et exec desact
            leftBtnText.set("Charger");
            leftBtnDisabled.set(false);
            rightBtnText.set("Exécuter");
            rightBtnDisabled.set(true);
        }
        else if (isLoaded && !isRunning) {
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
            executionVM.stopAuto();
            resetExecution();
        }
        updateState(); // update le visuel
    }

    public void clickRightButton() {
        if (executionVM.isAutoModeProperty().get()) {
            executionVM.playAuto(); // lance l'auto
            isRunning = true;
        } else {
            if (!isRunning) {
                isRunning = true; // bouton exec qui devient suivant
            } else {
                execute(); // on avance d'un step
            }
        }
        updateState();
    }

    public void notifyProgramChanged() {
        // dès qu'on modifie dans programme, on annule et on doit recharger à nouveau
        isLoaded = false;
        isRunning = false;
        executionVM.stopAuto();
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
            // recup le texte envoyé par la vue
            String val = amount.get();

            if (val == null || val.trim().isEmpty()) {
                throw new RuntimeException("Erreur valeur");
            }

            program.changeAmount(instru, amount.get());
            errorMessage.set("");
            isRunning = false;
            isLoaded = false;
            updateState();

            indexExec.set(-1); // remet l'exécution à zéro
            program.reset(); // replace la tortue
            coordX.set(program.getPen().getCoord().getX());
            coordY.set(program.getPen().getCoord().getY());
        } catch(RuntimeException e) {
            errorMessage.set(e.getMessage());
        }
    }

    /*
    si c'est une declaration on affiche le nom de la nouvelle variable (newVarName)
    si c'est une autre instruction, on affiche le parametre (soit nom de la variable soit un entier) sousforme de string
     */
    public String getParameterInstru() {
        int index = getIndexProperty().get();
        Instruction instru = instructions.get(index);

        if( instru.getType() == InstructionType.VAR_DECLARATION) {
            return instru.getNewVarName();
        }

        Value param = instru.getParameter();
        return param == null ? "" : param.toString();
    }

    public boolean isPenAction() {
        int index = getIndexProperty().get();
        Instruction instru = instructions.get(index);

        return instru.getType() == InstructionType.PEN_UP || instru.getType() == InstructionType.PEN_DOWN;
    }

    private void updateTurtleInfoText() {
        double x = coordX.get();
        double y = coordY.get();
        int direction = getOrientation();

        // On compense pour l'affichage (-90 + 90 = 0)
        double displayDirection = direction + 90;

        // Petite sécurité pour garder l'angle affiché entre -180° et 180°
        if (displayDirection > 180) {
            displayDirection -= 360;
        }

        turtleInfoText.set(String.format(java.util.Locale.FRENCH, "Tortue : x = %.1f, y = %.1f, direction = %.1f °", x, y, (double) displayDirection));
    }

    public boolean executeStep() {
        if (indexExec.get() < getInstru().size() - 1) {
            execute();
            updateState();
            return true; // le programme continue
        }
        return false; // le programme est fini
    }

    public void setOnStepExecuted(Runnable onStepExecuted) {
        this.onStepExecuted = onStepExecuted;
    }

    public void changeAssignment(String input1, String input2){
        int index = getIndexProperty().get();
        if (index < 0 || index >= getInstru().size()) {
            return;
        }

        Instruction instru = getInstru().get(index);

        try {
            if (input1 == null || input1.isEmpty()) {
                throw  new RuntimeException("Nom de variable invalide");
            }
            if( input2 == null || input2.isEmpty()) {
                throw new RuntimeException("Valeur invalide");
            }

            program.assignVariable(instru, input1, input2);

            errorMessage.set("");
            isRunning = false;
            isLoaded = false;
            updateState();

            indexExec.set(-1); // remet l'exécution à zéro
            program.reset(); // replace la tortue
            coordX.set(program.getPen().getCoord().getX());
            coordY.set(program.getPen().getCoord().getY());
        } catch (RuntimeException e){
            errorMessage.set(e.getMessage());
        }
    }

    public void changeIncrement(String varName, String stepStr){
        int index = getIndexProperty().get();
        if (index < 0 || index >= getInstru().size()) return;

        Instruction instru = getInstru().get(index);

        try {
            if (varName == null || varName.isEmpty()) throw new RuntimeException("Nom de variable invalide");
            if (stepStr == null || stepStr.isEmpty()) throw new RuntimeException("Valeur invalide");

            program.incrementVariable(instru, varName.trim(), stepStr.trim());

            errorMessage.set("");
            isRunning = false;
            isLoaded = false;
            updateState();
            indexExec.set(-1);
            program.reset();
            coordX.set(program.getPen().getCoord().getX());
            coordY.set(program.getPen().getCoord().getY());
        } catch (RuntimeException e){
            errorMessage.set(e.getMessage());
        }
    }

    public String getIncrementVarName() {
        Instruction instru = instructions.get(getIndexProperty().get());
        if (instru.getType() == InstructionType.INCREMENT_VARIABLE) return ((VarAssignment) instru).getVarName();
        return "";
    }

    public String getIncrementStep() {
        Instruction instru = instructions.get(getIndexProperty().get());
        if (instru.getType() == InstructionType.INCREMENT_VARIABLE) {
            int step = instru.getDifference();
            return step == 0 ? "" : String.valueOf(step);
        }
        return "";
    }

}
