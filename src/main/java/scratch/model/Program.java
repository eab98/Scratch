package scratch.model;


import java.io.File;
import java.io.PrintWriter;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Program {
    private final Character pen;
    private static final ObservableList<Instruction> catalogue = FXCollections.observableArrayList(
            new Translation("Avancer de", InstructionType.MOVE_FORWARD, 0, 0),
            new Rotation("Tourner à gauche de", InstructionType.TURN_LEFT,  0),
            new Rotation("Tourner à droite de", InstructionType.TURN_RIGHT,  0),
            new PenAction("Lever stylo", InstructionType.PEN_UP),
            new PenAction("Abaisser stylo", InstructionType.PEN_DOWN)
            );

    private static final ObservableList<Instruction> instructions = FXCollections.observableArrayList();
    private boolean penDown = true;   //true si PEN_DOWN, false si PEN_UP
    private final Shape shape = new Shape();

    public Program(Character pen) {
        this.pen = pen;
    }

    public Character getPen() {
        return pen;
    }

    public Shape getShape() {
        return shape;
    }

    public boolean isPenDown() {
        return penDown;
    }

    public void setPenDown(boolean penDown) {
        this.penDown = penDown;
    }

    public void addInstruction(int idx, int insertIndex) {

        Instruction instru = catalogue.get(idx);
        if (insertIndex >= 0 && insertIndex < instructions.size())
            instructions.add(insertIndex + 1, instru.newInstruction());
        else
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
        if (instr.getType() == InstructionType.PEN_DOWN || instr.getType() == InstructionType.PEN_UP) {
            setPenDown(((PenAction) instr).changePenStatus(this.penDown));
        } else {
            // sauvegarde la position de départ
            Coordinates initial = new Coordinates(pen.getCoord().getX(), pen.getCoord().getY());

            // déplace la tortue
            instr.execute(getPen());

            // sauvegarde la position d'arrivée et on trace la ligne si le pen est baissé
            if (isPenDown()) {
                Coordinates end = new Coordinates(pen.getCoord().getX(), pen.getCoord().getY());
                shape.addLine(new Line(initial, end));
            }
        }
    }


    // sauvegarder fichier
    public void saveToFile(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            // parcourt toutes les instructions du programme
            for (Instruction instr : instructions) {
                writer.println(instr.getType() + ";" + instr.getParameter());
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

                        if (modele.getType().toString().equals(typeAction)) {
                            this.addInstruction(i, -1);

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


    public static ObservableList<Instruction> getCatalogue() {
        return FXCollections.unmodifiableObservableList(catalogue);
    }

    public static ObservableList<Instruction> getInstructions() {
        return FXCollections.unmodifiableObservableList(instructions);

    }

    public void changeAmount(Instruction instru, int amount) {
        instru.setParameter(amount);
    }

    public void reset() {
        this.pen.reset();
        this.penDown = true;
    }

}
