package scratch.model;

/**
 * C'est la classe "Mère" de toutes les actions du jeu.
 * Le mot "abstract" (abstraite) signifie qu'on ne peut pas créer une "Instruction" générique.
 * On sera obligé de créer une action spécifique (comme une Translation ou une Rotation) qui héritera de cette classe.
 */
public abstract class Instruction {
    private final String name; // Le nom affiché à l'écran (ex: "Avancé de")
    private final InstructionType type; // Le mot-clé officiel du dictionnaire (ex: MOVE_FORWARD)

    private Value value; // Le paramètre (le chiffre 30, ou la variable "ma_var")

    // Le Constructeur : quand on crée une action, on lui donne son nom, son type et son paramètre
    public Instruction(String name, InstructionType type, Value value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public InstructionType getType() {
        return this.type;
    }

    public String getName() {
        return name;
    }

    public void execute(Character character) {
    }

    @Override
    public String toString() {
        return this.getName() + " ";
    }

    public abstract Instruction newInstruction();

    public abstract Instruction duplicate();

    public void setParameter(Value value) {
        this.value = value;
    }

    public Value getParameter() {
        return this.value;
    }

    public Value getValue() {
        return value;
    }


    //Transtypage

    public boolean changePenStatus(boolean currentStatus){
        throw new UnsupportedOperationException("Action interdite pour ce type d'instruction");
    }

    public String getNewVarName() {
        throw new UnsupportedOperationException("Action interdite pour ce type d'instruction");
    }

    public void setNewVarName(String newVarName) {
        throw new UnsupportedOperationException("Action interdite pour ce type d'instruction");
    }

    public void increment(Variable var, Value diff) {
        throw new UnsupportedOperationException("Action interdite pour ce type d'instruction");
    }

    public int getLoop() {
        throw new UnsupportedOperationException("Action interdite pour ce type d'instruction");
    }

    public void assign(Variable var, Value value){
        throw new UnsupportedOperationException("Action interdite pour ce type d'instruction");
    }

    public String getVarName(){
        throw new UnsupportedOperationException("Action interdite pour ce type d'instruction");

    }
    public int getDifference() {
        throw new UnsupportedOperationException("Action interdite pour ce type d'instruction");

    }

    public Object getAssignedValue() {
        throw new UnsupportedOperationException("Action interdite pour ce type d'instruction");
    }

    public void setVarName(String varName) {
        throw new UnsupportedOperationException("Action interdite pour ce type d'instruction");

    }

    public void setAssignedValue(Value assignedValue) {
        throw new UnsupportedOperationException("Action interdite pour ce type d'instruction");
    }

    public void setDifference(Value difference) {
        throw new UnsupportedOperationException("Action interdite pour ce type d'instruction");
    }
}
