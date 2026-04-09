package scratch.model;

public class Translation extends Movement{
    private int distance;


    public Translation(String name, InstructionType type, int direction, int distance) {
        super(name, type, direction);
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public Instruction newInstruction() {
        Translation t = new Translation("Avancer de", InstructionType.MOVE_FORWARD, 0, 30);
        t.setParameter("30");
        return t;
    }

    public void setDistance(int distance) {
        if(distance>0 && distance<=100) {
            this.distance = distance;
        }else{
            throw new RuntimeException("Erreur: La distance doit être comprise entre 1 et 100.");
        }
    }

    @Override
    public void execute(Program program){
        Character pen = program.getPen();
        int direction = pen.getOrientation();
        Coordinates origin = pen.getCoord();

        int dist = program.evaluate(this.getParameter());

        double x = Math.cos(Math.toRadians(direction)) * dist;
        double y = Math.sin(Math.toRadians(direction)) * dist;

        double x_in = origin.getX();
        double y_in = origin.getY();
        origin.setX(x_in + (int)x);
        origin.setY(y_in + (int)y);
    }

    @Override
    public String toString() {
        String param = this.getParameter();
        return super.toString() + (param != null && !param.isEmpty() ? param : "");
    }

    @Override
    public Instruction duplicate() {
        Translation clone = new Translation(this.getName(), this.getType(), this.getDirection(), this.getDistance());
        clone.setParameter(this.getParameter());
        return clone;
    }
}
