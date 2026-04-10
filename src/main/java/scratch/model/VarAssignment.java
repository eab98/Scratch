package scratch.model;

public class VarAssignment extends VarAction {

    public VarAssignment(String name, InstructionType type) {
        super(name, type);
    }

    public Instruction newInstruction(){
        return new VarAssignment("Assignation ", this.getType());
    }

    public void assign(Variable var, Value value){
        var.setValue(value.getValue());
    }

}
