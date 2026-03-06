package scratch.model;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class Program {
    private final Character pen;
    private final List<Instruction> instructions = new ArrayList<>();
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

    public void addInstruction(Instruction instr) {
        instructions.add(instr);
        //++instructionIndex;
    }

    public void deleteInstruction(int index) {
        instructions.remove(index);
        /*if (instructionIndex >= instructions.size())
            instructionIndex--;*/
    }

    public void execute(int instructionIndex) {
        if (instructionIndex < instructions.size()) {
            Instruction instr = instructions.get(instructionIndex);
            if (instr instanceof PenAction)
                setPenDown(((PenAction) instr).changePenStatus(this.penDown));
            else
                instr.execute(getPen());
        }
    }

    // sauvegarder fichier
    public void saveToFile(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            // parcourt toutes les instructions du programme
            for (Instruction instr : instructions) {
                writer.println(instr.getName() + ";0");
            }
            System.out.println("Sauvegarde réussie dans : " + file.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    // charger fichier
    public void loadFromFile(File file) {
        try (Scanner scanner = new Scanner(file)) {

            // vide le programme actuel
            this.instructions.clear();

            // lit ligne par ligne
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String[] morceaux = ligne.split(";"); // Sépare "FORWARD" et "30"

                if (morceaux.length == 2) {
                    String typeAction = morceaux[0];
                    int valeur = Integer.parseInt(morceaux[1]);

                    // crée la bonne instruction en fonction du texte
                    switch (typeAction) {
                        case "Avancer":
                            this.addInstruction(new Translation("Avancer", MovementType.FORWARD, valeur));
                            break;

                        case "Tourner":
                            this.addInstruction(new Rotation("Tourner", MovementType.RIGHT, valeur));
                            break;

                        case "PEN_UP":
                            this.addInstruction(new PenAction("Lever le stylo", PenMovement.PEN_UP));
                            break;

                        case "PEN_DOWN":
                            this.addInstruction(new PenAction("Abaisser le stylo", PenMovement.PEN_DOWN));
                            break;
                        default:
                            System.out.println("Action inconnue : " + typeAction);
                    }
                }
            }
            System.out.println("Fichier chargé avec succès !");

        } catch (Exception e) {
            System.err.println("Erreur de chargement : " + e.getMessage());
        }
    }
}
