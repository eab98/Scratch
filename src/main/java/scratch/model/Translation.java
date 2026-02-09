package scratch.model;

public class Translation extends Movement{
    private int distance;


    public Translation(String name, MovementType type, int direction) {
        super(name, type, direction);
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        if(distance>0 && distance<=100) {
            this.distance = distance;
        }
    }

    public Coordinates translate(Coordinates origin, int direction){
        double x = Math.cos(Math.toRadians(direction)) * distance;
        double y = Math.sin(Math.toRadians(direction)) * distance;
        double x_in = origin.getX();
        double y_in = origin.getY();
        origin.setX(x_in + x);
        origin.setY(y_in + y);
        return origin;
    }

}
