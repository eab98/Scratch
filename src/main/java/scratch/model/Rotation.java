package scratch.model;

public class Rotation extends Movement {


    // Le Constructeur : il transmet les infos au parent (Movement -> Instruction)
    public Rotation(String name, InstructionType type, Value direction) {
        super(name, type, direction);
    }


    /**
     * Cette méthode vérifie si l'angle choisi par l'utilisateur est correct.
     */
    public void setDirection(Value value) {
        int newDirection = value.getValue();
        // Règle imposée : on ne peut pas tourner de moins de 1 ou de plus de 180 d'un coup
        if (newDirection < 1 || newDirection > 180)
            throw new RuntimeException("Erreur: L'angle doit être compris entre 1 et 180 degrés.");

        super.setDirection(newDirection);
    }

    /*
    Attention, ici c'est le nouvel angle qu'on donne a l'instruction donc modif qui doit respecter les bornes,
     pas le résultat qui est l'orientation de la tortue
     */
    /**
     * L'exécution de la rotation.
     */
    @Override
    public void execute(Character pen) {
        // 1 : On regarde vers où la tortue regarde actuellement
        int initial_angle = pen.getOrientation();

        // 2 : On récupère la valeur dans la boîte (ex: 90)
        int modif = this.getParameter().getValue();

        // 3 : On vérifie encore une fois les bornes (sécurité)
        if(modif<1 || modif>180){
            throw new RuntimeException("Erreur: " + this + "\nL'angle doit être compris entre 1 et 180 degrés.");
        }

        // 4 : LOGIQUE DE DIRECTION :
        // Dans notre projet, si le type est TURN_LEFT, on soustrait l'angle (on tourne vers les négatifs)
        if (this.getType() == InstructionType.TURN_LEFT)
            modif = -modif;


        // 5 : On calcule le nouvel angle et on le donne à la tortue
        int newAngle = initial_angle + modif;
        pen.setOrientation(newAngle);


    }


    /**
     * La fabrique (Vitrine) : crée une rotation de 90 par défaut
     */
    @Override
    public Instruction newInstruction() {
        String name = "Tourner à " + (this.getType() == InstructionType.TURN_LEFT ? "gauche de" : "droite de");
        return new Rotation(name, this.getType(), new Value(90));

    }

    /**
     * Pour l'affichage dans la liste (ex: "Tourner à gauche de 90")
     */
    @Override
    public String toString() {
        Value p = getParameter();
        String msg = (p == null) ? "" : p.toString();
        return super.toString() + msg;
    }

    /**
     * La photocopieuse : clone l'instruction avec son angle actuel.
     */
    @Override
    public Instruction duplicate() {
        return new Rotation(this.getName(), this.getType(), this.getValue());
    }

    /**
     * Quand on change la boîte de paramètres, on déclenche la vérification setDirection.
     */
    public void setParameter(Value value) {
        super.setParameter(value);
        if (value != null) {
            setDirection(value.getValue());
        }
    }
}
