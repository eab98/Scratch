package scratch.model;

public class VarDeclaration extends VarAction {

    private String newVarName = "";
    public VarDeclaration(String name, InstructionType type) {
        super(name, type);
    }

    // ajoute depuis la palette au programme
    @Override
    public Instruction newInstruction(){
        return new VarDeclaration("Déclaration variable", this.getType());
    }

    @Override
    public Instruction duplicate() {
        return this;
    }

    public Variable declare(String name){
        return new Variable(name);
    }

    @Override
    public String toString() {
        return this.getName() + " : " + newVarName;
    }

    @Override
    public String getNewVarName() {
        return newVarName;
    }

    @Override
    public void setNewVarName(String newVarName) {
        this.newVarName = newVarName;
    }
}