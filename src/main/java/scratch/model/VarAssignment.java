package scratch.model;

public class VarAssignment extends VarAction {
    private String varName = "";
    private int difference = 0;
    private Value varValue;

    public VarAssignment(String name, InstructionType type) {
        super(name, type);
    }

    public VarAssignment(String name, InstructionType type, Value val) {
        super(name, type);
        this.setParameter(val);
    }

    public Instruction newInstruction(){
        return new VarAssignment(this.getName(), this.getType());
    }

    @Override
    public Instruction duplicate() {
        if (this.getType() == InstructionType.INCREMENT_VARIABLE)
            return new VarAssignment(this.getName(), this.getType(), this.getParameter());
        return this;
    }

    @Override
    public void assign(Variable var, Value value){
        var.setValue(value.getValue());
        this.setVarName(var.getName());
        this.varValue = value;
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

    @Override
    public String toString() {
        /*return this.getName() + " : " + this.getVarName();*//*(this.getVariable() == null ? "" : this.getVariable().getName()
                + (this.getType() == InstructionType.VAR_ASSIGNEMENT ? " = " + this.getVariable().getValue()
                : " de " + this.getDifference()));*/

        String instruName = this.getName();
        String msg = "";

        if(this.getType() == InstructionType.VAR_ASSIGNEMENT){
            msg = " : " + this.getVarName() + " = " + (this.varValue == null? "" : this.varValue);
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
