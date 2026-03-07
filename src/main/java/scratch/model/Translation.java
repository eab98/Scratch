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
        return new Translation("Avancer de", InstructionType.MOVE_FORWARD, 0, 30);
    }

    public void setDistance(int distance) {
        if(distance>0 && distance<=100) {
            this.distance = distance;
        }
    }

    @Override
    public void execute(Character pen){
        int direction = pen.getOrientation();
        Coordinates origin = pen.getCoord();
        double x = Math.cos(Math.toRadians(direction)) * distance;
        double y = Math.sin(Math.toRadians(direction)) * distance;

        double x_in = origin.getX();
        double y_in = origin.getY();
        origin.setX(x_in + (int)x);
        origin.setY(y_in + (int)y);
    }

    @Override
    public String toString() {
        String msg = this.distance == 0 ? "" : Integer.toString(this.distance);
        return super.toString() + msg;
    }

    @Override
    public Instruction duplicate() {
        return new Translation(this.getName(), this.getType(), this.getDirection(), this.getDistance());
    }

    @Override
    public void setParameter(int value) {
        this.setDistance(value);
    }

    @Override
    public int getParameter() {
        return this.getDistance();
    }
}
