package scratch.model;

public class TestModel {
    public static void main(String[] args) {

        Coordinates origin = new Coordinates(0, 0);
        Character pen = new Character(0, origin);
        Translation tr = new Translation("Avancer", MovementType.FORWARD, 0);
        tr.setDistance(30);
        tr.execute(pen);
        System.out.println(origin.getX());
        System.out.println(origin.getY());
        System.out.println();

        Rotation rot = new Rotation("Tourner", MovementType.RIGHT, 90);
        rot.execute(pen);
        tr.execute(pen);
        System.out.println(origin.getX());
        System.out.println(origin.getY());
    }
}
