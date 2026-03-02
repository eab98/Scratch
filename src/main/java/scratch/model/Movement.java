package scratch.model;

public abstract class Movement extends Instruction {
    private final MovementType type;
    private int direction;

    public Movement(String name, MovementType type, int direction) {
        super(name);
        this.type = type;
        this.direction = direction;
    }

    public MovementType getType() {
        return type;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Movement() {
        super(super.name);

    }
}
