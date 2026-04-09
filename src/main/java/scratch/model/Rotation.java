package scratch.model;

public class Rotation extends Movement {

    public Rotation(String name, InstructionType type, int direction) {
        super(name, type, direction);
    }

    @Override
    public void setDirection(int newDirection) {
        if (newDirection < 1 || newDirection > 180)
            throw new RuntimeException("Erreur: L'angle doit être compris entre 1 et 180 degrés.");

        super.setDirection(newDirection);
    }

    @Override
    public void execute(Program program) {
        Character pen = program.getPen();
        int initial_angle = pen.getOrientation();

        // lit le texte (90 ou nb par exemple)
        int modif = program.evaluate(this.getParameter());

        if (this.getType() == InstructionType.TURN_LEFT)
            modif = -modif;
        pen.setOrientation(initial_angle + modif);
    }

    @Override
    public Instruction newInstruction() {
        String name = "Tourner à " + (this.getType() == InstructionType.TURN_LEFT ? "gauche de" : "droite de");
        Rotation r = new Rotation(name, this.getType(), 90);
        r.setParameter("90");
        return r;
    }

    @Override
    public String toString() {
        String param = this.getParameter();
        return super.toString() + (param != null && !param.isEmpty() ? param : "");
    }

    @Override
    public Instruction duplicate() {
        Rotation clone = new Rotation(this.getName(), this.getType(), this.getDirection());
        clone.setParameter(this.getParameter());
        return clone;
    }
}
