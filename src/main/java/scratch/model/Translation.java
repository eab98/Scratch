package scratch.model;

/**
 * La classe Translation est l'action "Avancer de X".
 * Elle hérite de Movement (qui hérite lui-même de Instruction).
 */
public class Translation extends Movement{
    // La distance spécifique à la translation (en pixels)
    private int distance;

    // Le Constructeur
    public Translation(String name, InstructionType type, Value direction, int distance) {
        // On envoie le nom, le type et la boîte au parent Movement
        super(name, type, direction);
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    // on crée par défaut un bloc qui s'appelle "Avancer de", type MOVE_FORWARD, avec une valeur de 30.
    @Override
    public Instruction newInstruction() {
        return new Translation("Avancer de", InstructionType.MOVE_FORWARD, new Value(30), 30);
    }

    public void setDistance(Value value) {
        int amount = value.getValue();
        this.distance = amount;

        /*if(amount>0 && amount<=100) {
            this.distance = amount;
        }else{
            throw new RuntimeException("Erreur: La distance doit être comprise entre 1 et 100.");
        }*/
    }

    // C'est la méthode qui calcule la nouvelle position de la tortue sur l'écran.
    @Override
    public void execute(Character pen){
        // 1 : On demande à la tortue : "Vers où tu regardes et où es-tu en ce moment ?"
        int direction = pen.getOrientation();
        Coordinates origin = pen.getCoord();

        // 2 : On récupère la distance demandée par l'utilisateur (le chiffre dans la boîte)
        int currentDistance = this.getParameter().getValue();

        // 3 : Sécurité - On empêche les nombres négatifs ou trop grands pour ne pas sortir de l'écran
        if(currentDistance<=0 || currentDistance>100){
            // S'il y a une erreur, on "jette" une exception (le programme s'arrête et on affichera l'erreur en rouge plus tard)
            throw new RuntimeException("Erreur : " + this + "\nLa distance doit être comprise entre 1 et 100");
        }

        // Note : Math.toRadians transforme les degrés (0-360) en radians (0-2PI) car Java préfère les radians.
        double x = Math.cos(Math.toRadians(direction)) * currentDistance;
        double y = Math.sin(Math.toRadians(direction)) * currentDistance;

        // 5 : On ajoute le déplacement calculé à la position actuelle de la tortue
        double x_in = origin.getX();
        double y_in = origin.getY();

        origin.setX(x_in + (int)x);
        origin.setY(y_in + (int)y);
    }


    /**
     * Modifie le texte affiché dans la liste.
     * ex: super.toString() donne "Avancer de " et p.toString() donne "30". Résultat : "Avancer de 30"
     */
    @Override
    public String toString() {
        Value p = getParameter();
        String msg = (p == null) ? "" : p.toString();
        return super.toString() + msg;
    }

    /**
     * On crée un nouveau mouvement avec exactement les mêmes valeurs que l'actuel.
     */
    @Override
    public Instruction duplicate() {
        return new Translation(this.getName(), this.getType(), this.getValue(), this.getDistance());

    }

    /**
     * Quand on change la boîte (le paramètre), on met aussi à jour la distance interne.
     */
    @Override
    public void setParameter(Value value) {
        super.setParameter(value);
        if (value != null) {
            setDistance(value);
        }
    }

}
