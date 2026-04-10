package scratch.model;

class VarAction extends Instruction {

    private final Variable variable;

    public VarAction(String name, InstructionType type) {
        super(name, type, null);
        this.variable = null;
    }
}
