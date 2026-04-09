package scratch.model;

public abstract class Instruction {
    private final String name;
    private final InstructionType type;

    private final Variable variable;
    private String stringParameter = "";

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

    public void execute(Program program) {
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

    public void setParameter(String value) {
        this.stringParameter = value;
    }

    public String getParameter() {
        return this.stringParameter;
    }

    public Variable getVariable() {
        return variable;
    }
}
