package scratch.model;

public abstract class Instruction {
    private final String name;

    public Instruction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
