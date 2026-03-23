package scratch.model;

public class Character {
    private int orientation;
    private Coordinates coord;

    private final int initialOrientation;
    private final double initialX;
    private final double initialY;

    public Character(int initialOrientation, Coordinates origin) {
        this.orientation = initialOrientation;
        this.coord = origin;

        this.initialOrientation = initialOrientation;
        this.initialX = origin.getX();
        this.initialY = origin.getY();
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public Coordinates getCoord() {
        return coord;
    }

    public void setCoord(Coordinates coord) {
        this.coord = coord;
    }

    public void reset() {
        this.orientation = initialOrientation;
        this.coord.setX(initialX);
        this.coord.setY(initialY);
    }
}
