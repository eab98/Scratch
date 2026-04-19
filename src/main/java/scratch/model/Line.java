package scratch.model;

/**
 * Une Line (Ligne), c'est tout simplement un trait de feutre entre un point A et un point B.
 */
public class Line {
    private final Coordinates origin;
    private final Coordinates end;

    public Line(Coordinates origin, Coordinates end) {
        this.origin = origin;
        this.end = end;
    }
}
