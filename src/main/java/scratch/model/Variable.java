package scratch.model;

/**
 * Une Variable est une Value améliorée
 * Elle contient toujours un chiffre (hérité de Value), mais elle a en plus un "Nom".
 */
public class Variable extends Value{
    private String name;

    // Le constructeur : quand on crée une variable, on lui donne juste un nom, sa valeur de base est 0.
    public Variable(String name) {
        super(0); // On donne la valeur 0 au parent (Value)
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        // vérifie que le nom de la variable commence par une lettre (ou _)
        // et ne contient pas de caractères bizarres comme @, %, espace, etc.
        if(name.matches("^[a-zA-Z_][a-zA-Z0-9_]*$"))  this.name = name;
    }

    // Ex: Si ma variable s'appelle "nb_tour" et vaut 5, on veut afficher "Avancer de nb_tour", pas "Avancer de 5".
    @Override
    public String toString() {
        return name;
    }
}
