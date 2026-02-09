package scratch.model;

public class TestModel {
    static void main() {
        Coordinates origin = new Coordinates(0, 0);
        Translation tr = new Translation("Avancer", MovementType.FORWARD, 0);
        tr.setDistance(30);
        origin = tr.translate(origin, 0);
        System.out.println(origin.getX());
        System.out.println(origin.getY());
        System.out.println();

        origin = tr.translate(origin, 180);
        System.out.println(origin.getX());
        System.out.println(origin.getY());
    }
}
