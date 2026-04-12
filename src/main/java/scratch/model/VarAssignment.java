package scratch.model;

public class VarAssignment extends VarAction {
    private String varName = "";
    private int difference = 0;

    public VarAssignment(String name, InstructionType type) {
        super(name, type);
    }

    public Instruction newInstruction(){
        return new VarAssignment(this.getName(), this.getType());
    }

    public void assign(Variable var, Value value){
        var.setValue(value.getValue());
        this.setVarName(var.getName());
    }

    // méthode pour stocker l'incrémentation
    public void increment(Variable var, int diff) {
        this.setVarName(var.getName());
        this.setDifference(diff);
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

    @Override
    public String toString() {
        return this.getName() + " : " + this.getVarName();/*(this.getVariable() == null ? "" : this.getVariable().getName()
                + (this.getType() == InstructionType.VAR_ASSIGNEMENT ? " = " + this.getVariable().getValue()
                : " de " + this.getDifference()));*/
    }
}
