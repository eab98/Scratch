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

    // action principale de l'instruction
    @Override
    public void execute(Program program) {
        String varName = this.getParameter();

        // si le nom a bien été encodé par l'utilisateur
        if (varName != null && !varName.trim().isEmpty()) {
            program.getVariables().put(varName.trim(), new Variable(varName.trim()));
        }
    }

    @Override
    public String toString() {
        String param = this.getParameter();
        return this.getName() + (param != null && !param.isEmpty() ? " " + param : "");
    }

    // bouton "Dupliquer"
    @Override
    public Instruction duplicate() {
        VarDeclaration clone = new VarDeclaration(this.getName(), this.getType());
        clone.setParameter(this.getParameter());
        return clone;
    }
}