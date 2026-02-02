package scratch.model;

public class Character {
    private int orientation;

    public Character(int initialOrientation) {
        this.orientation = initialOrientation;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
