package scratch.model;

public class VarAssignment extends VarAction {
    private String varName = "";
    private int difference = 0;
    private Value varValue;

    private int assignedValue = 0;

    public VarAssignment(String name, InstructionType type) {
        super(name, type);
    }

    public Instruction newInstruction(){
        return new VarAssignment(this.getName(), this.getType());
    }

    @Override
    public void assign(Variable var, Value value){
        // on stocke la valeur dans l'instruction pour le moment de l'exécution
        this.setVarName(var.getName());
        this.assignedValue = value.getValue();
    }

    // méthode pour stocker l'incrémentation
    @Override
    public void increment(Variable var, int diff) {
        this.setVarName(var.getName());
        this.setDifference(diff);
    }

    @Override
    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    @Override
    public int getDifference() {
       return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

    public int getAssignedValue() { return assignedValue; }

    @Override
    public String toString() {
        /*return this.getName() + " : " + this.getVarName();*//*(this.getVariable() == null ? "" : this.getVariable().getName()
                + (this.getType() == InstructionType.VAR_ASSIGNEMENT ? " = " + this.getVariable().getValue()
                : " de " + this.getDifference()));*/

        String instruName = this.getName();
        String msg = "";

        if(this.getType() == InstructionType.VAR_ASSIGNEMENT){
            msg = " : " + this.getVarName() + " = " + this.assignedValue;
        } else if (this.getType() == InstructionType.INCREMENT_VARIABLE){
            if(difference == 0){
                msg = " : " + this.getVarName() + " de ";
            }else {
                msg = " : " + this.getVarName() + " de " + this.difference;
            }
        }

        return instruName + " " + msg;
    }
}
