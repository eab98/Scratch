package scratch.model;

public class PenAction extends Instruction {

    private final PenMovement movement;

    public PenAction(String name, PenMovement move) {
        super(name);
        this.movement = move;
    }

    public PenMovement getMovement() {
        return movement;
    }

    public PenAction newPenAction() {
        return new PenAction(this.getName(), this.getMovement());
    }

    public boolean changePenStatus(boolean status) {
        if (status && movement == PenMovement.PEN_DOWN || !status && movement == PenMovement.PEN_UP)
            return status;
        return !status;
    }
}
