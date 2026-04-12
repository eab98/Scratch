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
            new VarAssignment("Assignation", InstructionType.VAR_ASSIGNMENT),
            new VarAssignment("Inc/Dec variable", InstructionType.INCREMENT_VARIABLE),
            new Translation("Avancer de", InstructionType.MOVE_FORWARD, new Value(0), 0),
            new Rotation("Tourner à gauche de", InstructionType.TURN_LEFT,  new Value(0)),
            new Rotation("Tourner à droite de", InstructionType.TURN_RIGHT,  new Value(0)),
            new Repeat("Répéter", InstructionType.REPEAT, new Value(0)),
            new Repeat("Fin Répéter", InstructionType.END_REPEAT, new Value(0)),
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

    /*on part du end pour retrouver le start et recommencer la boucle
    mais pour les boucles imbriquees, on peut trouver des start qui ne serait pas le start correspondant à notre end
    logique par niveau -> a chaque fois qu'on rencontre un end autre que celui en parametre, on increment le niveau car il
    s'agit d'une autre boucle
    */

    public int getLoopStart(int end) {
        int level = 0;

        for (int i = end - 1; i >= 0; i--) {
            Instruction instr = instructions.get(i);

            if (instr.getType() == InstructionType.END_REPEAT) {
                level++;
            } else if (instr.getType() == InstructionType.REPEAT) {
                //c'est le start qui correspond au end en param
                if (level == 0) {
                    return i;
                }
                //c'est un start d'une boucle imbriquee donc tout ce qui se trouve au-delà apartient à un niveau inférieur
                else {
                    level--;
                }
            }
        }
        return 0;
    }

    public boolean loopEnded(int idx) {
        Instruction loop = instructions.get(idx);
        return loop.getType() == InstructionType.REPEAT && loop.getLoop() <= 0;
    }

    public boolean allLoopsClosed() {
        int level = 0;
        //int end = 0;
        for (Instruction instr: instructions) {
            if (instr.getType() == InstructionType.REPEAT && level >= 0)
                ++level;
            else if (instr.getType() == InstructionType.END_REPEAT)
                --level;
        }
        return level == 0;
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

        } else if (instr.getType() == InstructionType.INCREMENT_VARIABLE) {
            Variable varToInc = variables.get(instr.getVarName());
            if (varToInc != null) {
                varToInc.setValue(varToInc.getValue() + instr.getDifference());
            }

        } else if (instr.getType() == InstructionType.VAR_ASSIGNMENT) {
            VarAssignment assignInstr = (VarAssignment) instr;
            Variable varToAssign = variables.get(assignInstr.getVarName());
            if (varToAssign != null) {
                varToAssign.setValue(assignInstr.getAssignedValue());
            }
        }

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
                switch (instr.getType()) {
                    case VAR_DECLARATION:
                        writer.println(instr.getType() + ";" + instr.getNewVarName());
                        break;
                    case VAR_ASSIGNMENT:
                        VarAssignment assign = (VarAssignment) instr;
                        writer.println(instr.getType() + ";" + assign.getVarName() + ";" + assign.getAssignedValue());
                        break;
                    case INCREMENT_VARIABLE:
                        VarAssignment inc = (VarAssignment) instr;
                        writer.println(instr.getType() + ";" + inc.getVarName() + ";" + inc.getDifference());
                        break;
                    case MOVE_FORWARD:
                    case TURN_LEFT:
                    case TURN_RIGHT:
                    case REPEAT:
                        // récupère soit le nombre, soit le nom de la variable (si c'est "nb" par ex)
                        String paramStr = (instr.getParameter() != null) ? instr.getParameter().toString() : "0";
                        writer.println(instr.getType() + ";" + paramStr);
                        break;
                    default:
                        // pour PEN_UP, PEN_DOWN, END_REPEAT (qui n'ont pas de paramètre)
                        writer.println(instr.getType() + ";");
                        break;
                }
            }
            System.out.println("Fichier sauvegardé avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur de sauvegarde : " + e.getMessage());
        }
    }

    // charger fichier
    public void loadFromFile(File file) {
        try (Scanner scanner = new Scanner(file)) {

            // vide le programme et nettoie la mémoire des variables
            this.empty();
            declaredVariablesBeforeExec.clear();

            // lit ligne par ligne
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                if (ligne.trim().isEmpty()) continue; // ignore les lignes vides

                String[] morceaux = ligne.split(";");
                String typeAction = morceaux[0];

                // crée la bonne instruction en fonction du texte
                for (int i = 0; i < catalogue.size(); i++) {
                    Instruction modele = catalogue.get(i);

                    if (modele.getType().toString().equals(typeAction)) {
                        this.addInstruction(i, -1);
                        Instruction nouvelleAction = instructions.get(instructions.size() - 1);

                        // si la ligne contient des paramètres (donc > 1 morceau)
                        if (morceaux.length > 1) {
                            switch (nouvelleAction.getType()) {
                                case VAR_DECLARATION:
                                    String varName = morceaux[1].trim();
                                    nouvelleAction.setNewVarName(varName);
                                    // On réenregistre la variable dans le moteur
                                    declaredVariablesBeforeExec.put(varName, new Variable(varName));
                                    break;
                                case VAR_ASSIGNMENT:
                                    if (morceaux.length >= 3) {
                                        VarAssignment assignInstr = (VarAssignment) nouvelleAction;
                                        assignInstr.setVarName(morceaux[1].trim());
                                        assignInstr.setAssignedValue(Integer.parseInt(morceaux[2].trim()));
                                    }
                                    break;
                                case INCREMENT_VARIABLE:
                                    if (morceaux.length >= 3) {
                                        VarAssignment incInstr = (VarAssignment) nouvelleAction;
                                        incInstr.setVarName(morceaux[1].trim());
                                        incInstr.setDifference(evaluate(morceaux[2].trim()));
                                    }
                                    break;
                                case MOVE_FORWARD:
                                case TURN_LEFT:
                                case TURN_RIGHT:
                                case REPEAT:
                                    // evaluate() s'occupe de savoir si c'est "30" ou "nb"
                                    String paramVal = morceaux[1].trim();
                                    Value evaluated = evaluate(paramVal);
                                    if (evaluated != null) {
                                        nouvelleAction.setParameter(evaluated);
                                    }
                                    break;
                            }
                        }
                        break; // on a trouvé, on passe à la prochaine ligne
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

            // On ne la crée que si elle n'existe pas déjà
            if (!declaredVariablesBeforeExec.containsKey(param)) {
                declaredVariablesBeforeExec.put(param, new Variable(param));
            }

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

        if (!allLoopsClosed())
            return false;
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

    public void assignVariable(Instruction instru, String variable, String valueText){
            Variable var = declaredVariablesBeforeExec.get(variable);
            if (var == null) {
                throw new RuntimeException("Variable non déclarée");
            }

            Value evaluated = evaluate(valueText);
            if (evaluated == null) {
                throw new RuntimeException("Valeur invalide");
            }

            if (instru.getType() == InstructionType.VAR_ASSIGNMENT) {
                instru.setParameter(evaluated);
                instru.assign(var, evaluated);
            }
        }


    public void incrementVariable(Instruction instru, String variable, String stepStr){
        Variable var = declaredVariablesBeforeExec.get(variable);
        if (var == null) throw new RuntimeException("Variable non déclarée");

        // On prend directement la valeur tapée dans la case
        Value stepVal = this.evaluate(stepStr);

        if(instru.getType() == InstructionType.INCREMENT_VARIABLE){
            instru.increment(var, stepVal);
        }
        //((VarAssignment) instru).increment(var, stepVal);
    }

    public void resetVariables() {
        // remet la valeur de chaque variable déclarée à 0
        for (Variable v : declaredVariablesBeforeExec.values()) {
            v.setValue(0);
        }
        // vide la map d'exece
        variables.clear();
    }

}
