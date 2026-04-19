package scratch.model;

/**
 * L'action de créer une nouvelle variable.
 */
public class VarDeclaration extends VarAction {

    // L'étique de la nouvelle boîte
    private String newVarName = "";
    public VarDeclaration(String name, InstructionType type) {
        super(name, type);
    }

    // La vitrine : fabrique un bloc de déclaration
    // ajoute depuis la palette au programme
    @Override
    public Instruction newInstruction(){
        return new VarDeclaration("Déclaration variable", this.getType());
    }

    @Override
    public Instruction duplicate() {
        return this; // On renvoie le bloc lui-même
    }

    // L'action physique de fabriquer la boîte Variable
    public Variable declare(String name){
        return new Variable(name);
    }

    // Affichage à l'écran : ex "Déclaration variable : score"
    @Override
    public String toString() {
        return this.getName() + " : " + newVarName;
    }

    @Override
    public String getNewVarName() {
        return newVarName;
    }

    /**
     * Quand l'utilisateur tape le nom de sa variable, on fait appel au Regex pour vérifier si le nom est légal !
     */
    @Override
    public void setNewVarName(String newVarName) {
        if(!newVarName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            // Si le nom est interdit (ex: commence par un chiffre), erreur
            throw new RuntimeException("Format invalide");
        }
        this.newVarName = newVarName;
    }
}