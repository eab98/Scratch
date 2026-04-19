package scratch.model;

/**
 * Une classe intermédiaire qui regroupe tous les déplacements.
 * Elle est "abstract" car un "Mouvement" générique n'existe pas, il faut que ce soit une Translation ou une Rotation.
 */
public abstract class Movement extends Instruction {
    private int direction;

    public Movement(String name, InstructionType type, Value value) {
        // Le mot-clé "super" appelle le constructeur de la classe Mère (Instruction)
        super(name, type, value);

        // Si on nous donne une valeur, on l'extrait de la boîte pour la stocker dans 'direction'
        if(value != null) this.direction = value.getValue();
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
