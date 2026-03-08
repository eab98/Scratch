package scratch.model;

public class Character {
    private int orientation;
    private Coordinates coord;

    public Character(int initialOrientation, Coordinates origin) {
        this.orientation = initialOrientation;
        this.coord = origin;
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
}
