package scratch.model;

public class VarAssignment extends VarAction {
    private String varName = "";

    public VarAssignment(String name, InstructionType type) {
        super(name, type);
    }

    public Instruction newInstruction(){
        return new VarAssignment("Assignation ", this.getType());
    }

    public void assign(Variable var, Value value){
        var.setValue(value.getValue());
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }
}
