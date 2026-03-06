package scratch.model;


import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Program {
    private final Character pen;
    private static final ObservableList<Instruction> catalogue = FXCollections.observableArrayList(
            new Translation("Avancer de", MovementType.FORWARD, 0, 0),
            new Rotation("Tourner à gauche de", MovementType.LEFT,  0),
            new Rotation("Tourner à droite de", MovementType.RIGHT,  0),
            new PenAction("Lever stylo", PenMovement.PEN_UP),
            new PenAction("Abaisser stylo", PenMovement.PEN_DOWN)
            );

    private static final ObservableList<Instruction> instructions = FXCollections.observableArrayList();
    //private int instructionIndex = 0;
    private boolean penDown = true;   //true si PEN_DOWN, false si PEN_UP

    public Program(Character pen) {
        this.pen = pen;
    }

    public Character getPen() {
        return pen;
    }

    public boolean isPenDown() {
        return penDown;
    }

    public void setPenDown(boolean penDown) {
        this.penDown = penDown;
    }

    public void addInstruction(int idx) {

        Instruction instru = catalogue.get(idx);
        instructions.add(instru.newInstruction());
    }

    public void up(int idx) {
        if(idx > 0 && idx < instructions.size()) {
            Instruction instru = instructions.set(idx, instructions.get(idx - 1));
            instructions.set(idx-1, instru);
        }
    }

    public void down(int idx) {
        if(idx>=0 && idx < instructions.size() - 1) {
            Instruction instru = instructions.set(idx, instructions.get(idx + 1));
            instructions.set(idx + 1, instru);
        }
    }

    public void deleteInstruction(int index) {
        instructions.remove(index);

    }

    public void duplicate(int idx){
        if(idx >= 0 && idx < instructions.size()) {
            Instruction instru = instructions.get(idx);
            instructions.add(instru.duplicate());
        }
    }

    public void execute(Instruction instr) {
        if (instr instanceof PenAction)
            setPenDown(((PenAction) instr).changePenStatus(this.penDown));
        else
            instr.execute(getPen());
    }


    // sauvegarder fichier
    public void saveToFile(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            // parcourt toutes les instructions du programme
            for (Instruction instr : instructions) {
                writer.println(instr.getName() + ";" + instr.getParameter());
            }
            System.out.println("Fichier sauvegardé avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur de sauvegarde : " + e.getMessage());
        }
    }

    // charger fichier
    public void loadFromFile(File file) {
        try (Scanner scanner = new Scanner(file)) {

            // vide le programme qu'on a
            this.empty();

            // lit ligne par ligne
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String[] morceaux = ligne.split(";"); // Sépare "FORWARD" et "30"

                if (morceaux.length == 2) {
                    String typeAction = morceaux[0];
                    int valeur = Integer.parseInt(morceaux[1]);

                    // crée la bonne instruction en fonction du texte (ON CHERCHE L'INDEX DANS LE CATALOGUE)
                    for (int i = 0; i < catalogue.size(); i++) {
                        Instruction modele = catalogue.get(i);

                        if (modele.getName().equals(typeAction)) {
                            this.addInstruction(i);

                            // on ajoute à la fin de notre liste d'instruction
                            Instruction nouvelleAction = instructions.get(instructions.size() - 1);

                            // on injecte la valeur
                            nouvelleAction.setParameter(valeur);

                            break; // on a trouvé, on passe à la prochaine ligne
                        }
                    }
                }
            }
            System.out.println("Fichier chargé avec succès !");

        } catch (Exception e) {
            System.err.println("Erreur de chargement : " + e.getMessage());
        }
    }

    public void empty(){
        instructions.clear();
    }



//    public ObservableList<Instruction> getCatalogue() {
//        return catalogue;
//    }

    public static ObservableList<Instruction> getCatalogue() {
        return FXCollections.unmodifiableObservableList(catalogue);
    }

    public static ObservableList<Instruction> getInstructions() {
        return FXCollections.unmodifiableObservableList(instructions);

    }
}
