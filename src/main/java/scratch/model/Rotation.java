package scratch.model;

public class Rotation extends Movement {


    public Rotation(String name, InstructionType type, Value direction) {
        super(name, type, direction);
    }


    public void setDirection(Value value) {
        int newDirection = value.getValue();
        if (newDirection < 1 || newDirection > 180)
            throw new RuntimeException("Erreur: L'angle doit être compris entre 1 et 180 degrés.");

        super.setDirection(newDirection);
    }

    @Override
    public void execute(Character pen) {
        int initial_angle = pen.getOrientation();

        int modif = this.getParameter().getValue();

        if (this.getType() == InstructionType.TURN_LEFT)
            modif = -modif;
        pen.setOrientation(initial_angle + modif);
    }

    @Override
    public Instruction newInstruction() {
        String name = "Tourner à " + (this.getType() == InstructionType.TURN_LEFT ? "gauche de" : "droite de");
        return new Rotation(name, this.getType(), new Value(90));

    }

    @Override
    public String toString() {
        Value p = getParameter();
        String msg = (p == null) ? "" : p.toString();
        return super.toString() + msg;
    }

    @Override
    public Instruction duplicate() {
        return new Rotation(this.getName(), this.getType(), this.getValue());
    }


    public void setParameter(Value value) {
        super.setParameter(value);
        if (value != null) {
            setDirection(value.getValue());
        }
    }
}
