package scratch.model;

public class TestModel {
    public static void main(String[] args) {

        Coordinates origin = new Coordinates(0, 0);
        Character pen = new Character(0, origin);
        Program program = new Program(pen);
        Translation tr = new Translation("Avancer", MovementType.FORWARD, 0);
        tr.setDistance(30);
        program.addInstruction(tr);
        Rotation rot = new Rotation("Tourner", MovementType.RIGHT, 90);
        program.addInstruction(rot);
        program.addInstruction(tr);
        program.execute(0);
        program.execute(1);
        program.execute(2);
        System.out.println(origin.getX());
        System.out.println(origin.getY());
    }
}
