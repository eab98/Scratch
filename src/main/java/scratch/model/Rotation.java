package scratch.model;

public class Rotation extends Movement {

    public Rotation(String name, MovementType type, int direction) {
        super(name, type, direction);
    }

    @Override
    public void setDirection(int newDirection) {
        if (newDirection < 1 || newDirection > 180)
            throw new RuntimeException("Erreur: L'angle doit être compris entre 1 et 180 degrés.");

        super.setDirection(newDirection);
    }

    @Override
    public void execute(Character pen) {
        int initial_angle = pen.getOrientation();
        int modif = getDirection();
        if (this.getType() == MovementType.RIGHT)
            modif = -modif;
        pen.setOrientation(initial_angle + modif);
    }

    public Rotation newRotation(MovementType type) {
        String name = "Tourner à " + (type == MovementType.LEFT ? "gauche" : "droite");
        return new Rotation(name, type, 90);
    }

    @Override
    public String toString() {
        String msg = this.getDirection() == 0 ? "" : Integer.toString(this.getDirection());
        return super.toString() + msg;
    }

    @Override
    public Instruction duplicate() {
        return new Rotation(this.getName(), this.getType(), this.getDirection());
    }
}
