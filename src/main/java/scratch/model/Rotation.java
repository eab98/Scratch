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


}
