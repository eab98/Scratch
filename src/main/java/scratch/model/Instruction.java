package scratch.model;

public abstract class Instruction {
    private final String name;

    public Instruction(String name) {
        this.name = name;
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
}
