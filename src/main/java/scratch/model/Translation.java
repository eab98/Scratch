package scratch.model;

public class Translation extends Movement{
    private int distance;


    public Translation(String name, InstructionType type, Value direction, int distance) {
        super(name, type, direction);
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public Instruction newInstruction() {
        return new Translation("Avancer de", InstructionType.MOVE_FORWARD, new Value(0), 30);
    }

    public void setDistance(Value value) {
        int amount = value.getValue();
        this.distance = amount;

        /*if(amount>0 && amount<=100) {
            this.distance = amount;
        }else{
            throw new RuntimeException("Erreur: La distance doit être comprise entre 1 et 100.");
        }*/
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
        Value p = getParameter();
        String msg = (p == null) ? "" : p.toString();
        return super.toString() + msg;
    }

    @Override
    public Instruction duplicate() {
        return new Translation(this.getName(), this.getType(), this.getValue(), this.getDistance());

    }

    @Override
    public void setParameter(Value value) {
        super.setParameter(value);
        if (value != null) {
            setDistance(value);
        }
    }

}
