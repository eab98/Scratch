package scratch.model;

/**
 * Gère les boucles (Répéter X fois et Fin Répéter).
 */
public class Repeat extends Instruction {
    // La variable "loop" est notre compte à rebours interne
    private int loop;

    public Repeat(String name, InstructionType type, Value value) {
        super(name, type, value);
        // On initialise le compte à rebours avec la valeur demandée (ex: 4)
        loop = this.getValue().getValue();
    }

    /**
     * C'est ici la mécanique du compte à rebours
     */
    @Override
    public void execute(Character character) {
        // Sécurité : pas de boucles négatives
        if (loop < 0)
            throw new RuntimeException("Erreur : " + this + "\nLe nombre de répétitions doit être positif");

        // Si c'est bien le bloc de DEBUT de boucle
        if (this.getType() == InstructionType.REPEAT) {
            // S'il est arrivé à 0 (boucle terminée), on le remet à sa valeur de départ
            // pour la prochaine fois qu'on relancera le programme.
            if (loop == 0)
                loop = getValue().getValue();

            // On enlève 1 au compteur (le "clic" du minuteur)
            --loop;
        }
    }

    @Override
    public int getLoop() {
        return this.loop;
    }

    @Override
    public void setParameter(Value value) {
        super.setParameter(value);
        loop = this.getValue().getValue();
    }

    /**
     * La vitrine : crée un bloc par défaut
     */
    @Override
    public Instruction newInstruction() {
        // Par défaut, un bloc "Répéter" est configuré sur 4
        Value def = new Value(4);

        // MAIS si c'est le bloc "Fin Répéter", on le met à 0 car il n'a pas de paramètre visible
        if (this.getType() == InstructionType.END_REPEAT)
            def.setValue(0);

        return new Repeat(this.getName(), this.getType(), def);
    }

    @Override
    public String getNewVarName() {
        return super.getNewVarName();
    }

    @Override
    public Instruction duplicate() {
        return new Repeat(this.getName(), this.getType(), this.getValue());
    }

    /**
     * Gère l'affichage dans la liste visuelle au centre de l'écran
     */
    @Override
    public String toString() {
        if (this.getType() == InstructionType.REPEAT)
            return this.getName() + (this.getValue().getValue() == 0 ? (this.getParameter() == null ?
                "" : " " + this.getParameter()) :
                " " + this.getValue() ) + " fois";
        else
            return this.getName(); // Pour le bloc "Fin répéter"
    }
}
