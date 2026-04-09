package scratch.model;

public class VarDeclaration extends VarAction {

    public VarDeclaration(String name, InstructionType type) {
        super(name, type);
    }

    // ajoute depuis la palette au programme
    @Override
    public Instruction newInstruction(){
        return new VarDeclaration("Déclaration variable", this.getType());
    }

    public Variable declare(String name){
        return new Variable(name);
    }

    @Override
    public String toString() {
        String param = this.getValue().toString();
        return this.getName() + (param != null && !param.isEmpty() ? " " + param : "");
    }

}