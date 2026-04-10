package scratch.model;

public abstract class Instruction {
    private final String name;
    private final InstructionType type;

    private Value value;

    public Instruction(String name, InstructionType type, Value value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public InstructionType getType() {
        return this.type;
    }

    public String getName() {
        return name;
    }

    public void execute(Character character) {
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

    public void setParameter(Value value) {
        this.value = value;
    }

    public Value getParameter() {
        return this.value;
    }

    public Value getValue() {
        return value;
    }
}
