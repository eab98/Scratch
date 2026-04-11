package scratch.model;


import java.io.File;
import java.io.PrintWriter;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Program {
    private final Character pen;
    private static final ObservableList<Instruction> catalogue = FXCollections.observableArrayList(
            new VarDeclaration("Déclaration variable", InstructionType.VAR_DECLARATION),
            new VarAssignment("Assignation", InstructionType.VAR_ASSIGNEMENT),
            new Translation("Avancer de", InstructionType.MOVE_FORWARD, new Value(0), 0),
            new Rotation("Tourner à gauche de", InstructionType.TURN_LEFT,  new Value(0)),
            new Rotation("Tourner à droite de", InstructionType.TURN_RIGHT,  new Value(0)),
            new PenAction("Lever stylo", InstructionType.PEN_UP),
            new PenAction("Abaisser stylo", InstructionType.PEN_DOWN)
            );

    private static final ObservableList<Instruction> instructions = FXCollections.observableArrayList();
    private boolean penDown = true;   //true si PEN_DOWN, false si PEN_UP
    private final Shape shape = new Shape();

    private final Map<String, Variable> variables = new HashMap<>();
    private final Map<String, Variable> declaredVariablesBeforeExec = new HashMap<>();

    public Program(Character pen) {
        this.pen = pen;
    }

    public Character getPen() {
        return pen;
    }

    public Shape getShape() {
        return shape;
    }

    public Map<String, Variable> getVariables() {
        return variables;
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
        checkProgramValidity();
    }

    public void up(int idx) {
        if(idx > 0 && idx < instructions.size()) {
            Instruction instru = instructions.set(idx, instructions.get(idx - 1));
            instructions.set(idx-1, instru);
            checkProgramValidity();
        }
    }

    public void down(int idx) {
        if(idx>=0 && idx < instructions.size() - 1) {
            Instruction instru = instructions.set(idx, instructions.get(idx + 1));
            instructions.set(idx + 1, instru);
            checkProgramValidity();
        }
    }

    public void deleteInstruction(int index) {
        instructions.remove(index);
        checkProgramValidity();

    }

    public void duplicate(int idx){
        if(idx >= 0 && idx < instructions.size()) {
            Instruction instru = instructions.get(idx);
            instructions.add(instru.duplicate());
            checkProgramValidity();
        }
    }

    public void execute(Instruction instr) {
        if (instr.getType() == InstructionType.PEN_DOWN || instr.getType() == InstructionType.PEN_UP) {
            setPenDown(instr.changePenStatus(this.penDown));

        } else if (instr.getType() == InstructionType.VAR_DECLARATION) {
            String name = instr.getNewVarName();
            Variable declaredVariable = declaredVariablesBeforeExec.get(name);

            if(declaredVariable != null){
                variables.put(name, declaredVariable);
            }

        } /*else if (instr.getType() == InstructionType.VAR_ASSIGNEMENT) {
            String name = ((VarAssignment)instr).getVarName();
            Variable varToAssign = variables.get(name);
            if (varToAssign != null)
                ((VarAssignment) instr).assign(varToAssign, instr.getValue());
        }*/

        else {
            // sauvegarde la position de départ
            Coordinates initial = new Coordinates(pen.getCoord().getX(), pen.getCoord().getY());

            // déplace la tortue
            instr.execute(this.getPen());

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
                    String valeur = morceaux[1].trim();

                    // crée la bonne instruction en fonction du texte (ON CHERCHE L'INDEX DANS LE CATALOGUE)
                    for (int i = 0; i < catalogue.size(); i++) {
                        Instruction modele = catalogue.get(i);

                        if (modele.getType().toString().equals(typeAction)) {
                            this.addInstruction(i, -1);

                            // on ajoute à la fin de notre liste d'instruction
                            Instruction nouvelleAction = instructions.get(instructions.size() - 1);

                            // on injecte la valeur
                            //nouvelleAction.setParameter(valeur);

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
        variables.clear();
    }


    public static ObservableList<Instruction> getCatalogue() {
        return FXCollections.unmodifiableObservableList(catalogue);
    }

    public static ObservableList<Instruction> getInstructions() {
        return FXCollections.unmodifiableObservableList(instructions);

    }

    public void changeAmount(Instruction instru, String param) {
        //si on veut declarer alors l'input est le nom de la nouvelle variable
        if(instru.getType() == InstructionType.VAR_DECLARATION){

            instru.setNewVarName(param);
            declaredVariablesBeforeExec.put(param, new Variable(param));

        //sinon, on regarde si on donne à l'instru un entier -> on crée une value, si un string ->
        } else {
            Value evaluated = evaluate(param);
            if (evaluated == null) {
                throw new RuntimeException("Valeur ou variable invalide");
            }
            instru.setParameter(evaluated);
        }

        }




    //vérifie si le programme est correct (pas de PenActions de même type qui se suivent)
    public boolean checkProgramValidity(){
        boolean state = true; //etat fictif du pen sans execution

        //on délimite la zone de déclaration de variables
        int indexFirstinstruction = 0;
        int i = 0;
        while(i < instructions.size() && instructions.get(i).getType() == InstructionType.VAR_DECLARATION ){
            indexFirstinstruction = i + 1; // + 1 car on veut verifier apres les variables
            i++;
        }

        for(int j = indexFirstinstruction; j < instructions.size(); j++) {
            Instruction current = instructions.get(j);
            if((current.getType() == InstructionType.PEN_DOWN && state) ||
                    (current.getType() == InstructionType.PEN_UP && !state)
            || instructions.get(j).getType() == InstructionType.VAR_DECLARATION){
                return false;
            }else if((current.getType() == InstructionType.PEN_DOWN && !state) || (current.getType() == InstructionType.PEN_UP && state)){
                state = !state;
            }
        }
        return true;
    }


    public void reset() {
        this.pen.reset();
        this.penDown = true;
    }

    // traduit un texte en value
    public Value evaluate(String param) {
        if (param == null || param.trim().isEmpty()) return null;
        try {
            // essaie de voir si c'est un simple nombre
            return new Value(Integer.parseInt(param.trim()));
        } catch (NumberFormatException e) {
            // si ça plante, c'est que c'est du texte
            if (declaredVariablesBeforeExec.containsKey(param.trim())) {
                return declaredVariablesBeforeExec.get(param.trim());
            }
            return null;
        }
    }

    /*
    On vérifie si la variable qu'on veut donner en paramètre à une instruction a déjà été déclarée dans la zone de déclaration
    sans devoir passer par l'exécution du programme (vu que la variable n'est rééllement ajoutée au map qu'à l'exécution)
     */

    public Variable getVariableDeclared(String name) {
        return declaredVariablesBeforeExec.get(name);
    }

    public void assignVariable(Instruction instru, String variable, String value){
        Variable var = variables.get(variable);
        ((VarAssignment) instru).assign(var, new Value(Integer.parseInt(value)));

    }

}
