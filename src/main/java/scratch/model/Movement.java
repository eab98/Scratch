package scratch.model;

public abstract class Movement extends Instruction {
    private int direction;

    public Movement(String name, InstructionType type, int direction) {
        super(name, type);
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
