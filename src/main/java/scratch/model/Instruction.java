package scratch.model;

public abstract class Instruction {
    private final String name;
    private final InstructionType type;

    private Value value;

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

    public void increment(Variable var, int diff) {
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
}
