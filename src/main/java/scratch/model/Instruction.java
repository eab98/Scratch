package scratch.model;

public abstract class Instruction {
    private final String name;
    private final InstructionType type;

    private final Variable variable;

    public Instruction(String name, InstructionType type, Variable variable) {
        this.name = name;
        this.type = type;
        this.variable = variable;
    }

    public InstructionType getType() {
        return this.type;
    }

    public String getName() {
        return name;
    }

    public void execute(Character pen) {
    }

    @Override
    public String toString() {
        return this.getName() + " ";
    }

    public Instruction newInstruction() {
        return this;
    }

    public Instruction duplicate() {
        return this;
    }

    public void setParameter(int value) {
    }

    public int getParameter() {
        return 0;
    }

    public Variable getVariable() {
        return variable;
    }
}
