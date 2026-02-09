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
        double x = Math.cos(direction) * distance;
        double y = Math.sin(direction) * distance;
        origin.setX(x);
        origin.setY(y);
        return origin;
    }
}
