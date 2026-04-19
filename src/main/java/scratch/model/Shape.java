package scratch.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Shape représente le dessin complet (la forme).
 * C'est comme une boîte qui garde en mémoire TOUTES les lignes tracées par la tortue.
 */
public class Shape {
    // Un 'Set' est une liste spéciale qui empêche d'avoir des lignes en double
    private final Set<Line> lines = new HashSet<>();

    // Méthode pour ajouter un nouveau trait au dessin
    public void addLine(Line line) {
        lines.add(line);
    }
}
