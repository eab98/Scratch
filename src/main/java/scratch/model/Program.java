package scratch.model;


import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Program {
    // 1. LA TORTUE : Le personnage qui va bouger
    private final Character pen;

    // 2. LE CATALOGUE : C'est le tiroir de gauche sur notre écran.
    // Il contient un exemplaire "tout neuf" de chaque bloc possible.
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

    // 3. LA LISTE D'EXÉCUTION : C'est la zone au milieu de l'écran.
    // C'est ici qu'on stocke les blocs que l'utilisateur a glissés.
    private static final ObservableList<Instruction> instructions = FXCollections.observableArrayList();

    // 4. LES ÉTATS : Est-ce qu'on dessine ? Quelle est la forme actuelle ?
    private boolean penDown = true;   //true si PEN_DOWN, false si PEN_UP
    private final Shape shape = new Shape();

    // 5. LA MÉMOIRE : Où on range les variables créées par l'utilisateur
    private final Map<String, Variable> variables = new HashMap<>(); // pendant exec
    private final Map<String, Variable> declaredVariablesBeforeExec = new HashMap<>(); // avant exec

//    private boolean isError = false;

    // CONSTRUCTEUR
    public Program(Character pen) {
        this.pen = pen;
    }

    // 1. LES ACCESSEURS (Pour que l'écran puisse lire les infos)

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

    // 2. LES BOUTONS DE CONTRÔLE (Pour jouer avec les blocs)

    // Quand l'utilisateur glisse un bloc du catalogue vers sa liste
    public void addInstruction(int idx, int insertIndex) {

        Instruction instru = catalogue.get(idx);
        if (insertIndex >= 0 && insertIndex < instructions.size())
            instructions.add(insertIndex + 1, instru.newInstruction());
        else
            instructions.add(instru.newInstruction());
        checkProgramValidity();
    }

    // Le bouton "Monter" pour remonter un bloc dans la liste
    public void up(int idx) {
        if(idx > 0 && idx < instructions.size()) {
            Instruction instru = instructions.set(idx, instructions.get(idx - 1));
            instructions.set(idx-1, instru);
            checkProgramValidity();
        }
    }

    // Le bouton "Descendre" pour baisser un bloc
    public void down(int idx) {
        if(idx>=0 && idx < instructions.size() - 1) {
            Instruction instru = instructions.set(idx, instructions.get(idx + 1));
            instructions.set(idx + 1, instru);
            checkProgramValidity();
        }
    }

    // La "Poubelle" pour effacer un bloc de la liste
    public void deleteInstruction(int index) {
        instructions.remove(index);
        checkProgramValidity();

    }

    // La "Photocopieuse" pour dupliquer un bloc qui est déjà dans ta liste
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

    // Quand on arrive à un "Fin Répéter", remonte la liste pour retrouver le "Répéter" de départ
    public int getLoopStart(int end) {
        int level = 0;

        // On part du bloc "Fin" et on remonte la liste à l'envers (i--)
        for (int i = end - 1; i >= 0; i--) {
            Instruction instr = instructions.get(i);

            if (instr.getType() == InstructionType.END_REPEAT) {
                level++;
            } else if (instr.getType() == InstructionType.REPEAT) {
                //c'est le start qui correspond au end en param
                // C'est le "Répéter" qui correspond exactement à notre "Fin Répéter"
                if (level == 0) {
                    return i;
                }
                //c'est un start d'une boucle imbriquee donc tout ce qui se trouve au-delà apartient à un niveau inférieur
                // C'est le "Répéter" d'une autre boucle imbriquée à l'intérieur, on l'ignore
                else {
                    level--;
                }
            }
        }
        return 0;
    }

    // LE SYSTÈME DE BOUCLES (Répéter / Fin Répéter)

    // Vérifie si la boucle a fini de tourner (si son compteur est arrivé à 0)
    public boolean loopEnded(int idx) {
        Instruction loop = instructions.get(idx);
        return loop.getType() == InstructionType.REPEAT && loop.getLoop() <= 0;
    }

    // Vérifie si chaque bloc "Répéter" possède bien son "Fin Répéter"
    public boolean allLoopsClosed() {
        int level = 0;
        //int end = 0;
        for (Instruction instr: instructions) {
            if (instr.getType() == InstructionType.REPEAT && level >= 0)
                ++level;
            else if (instr.getType() == InstructionType.END_REPEAT)
                --level;
        }
        // Si level finit à 0, ça veut dire qu'il y a autant de débuts que de fins.
        return level == 0;
    }

    // LE MOTEUR D'EXÉCUTION (Le bouton Executer)
    public void execute(Instruction instr) {

        // Cas 1 : Le stylo
        if (instr.getType() == InstructionType.PEN_DOWN || instr.getType() == InstructionType.PEN_UP) {
            setPenDown(instr.changePenStatus(this.penDown));

            // Cas 2 : La création de la variable dans le jeu en direct
        } else if (instr.getType() == InstructionType.VAR_DECLARATION) {
            String name = instr.getNewVarName();
            Variable declaredVariable = declaredVariablesBeforeExec.get(name);

            if(declaredVariable != null){
                // On la copie de la préparation vers la mémoire en direct !
                variables.put(name, declaredVariable);
            }

            // Cas 3 : On modifie une variable (Inc/Dec)
        } else if (instr.getType() == InstructionType.INCREMENT_VARIABLE) {
            Variable varToInc = variables.get(instr.getVarName());
            if (varToInc != null) {
                varToInc.setValue(varToInc.getValue() + instr.getDifference());
            }

            // Cas 4 : On force la valeur d'une variable (Assignation)
        } else if (instr.getType() == InstructionType.VAR_ASSIGNMENT) {
            VarAssignment assignInstr = (VarAssignment) instr;
            Variable varToAssign = variables.get(assignInstr.getVarName());
            if (varToAssign != null) {
                varToAssign.setValue(assignInstr.getAssignedValue().getValue());
            }
        }

        // Cas 5 : Le mouvement (Avancer, Tourner...)
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
    // Il écrit la liste des blocs dans un fichier texte
    public void saveToFile(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            // parcourt toutes les instructions du programme. On lit la liste de blocs de haut en bas
            for (Instruction instr : instructions) {
                // Selon le type de bloc, on l'écrit différemment dans le fichier avec un ";" comme séparateur
                switch (instr.getType()) {
                    case VAR_DECLARATION:
                        writer.println(instr.getType() + ";" + instr.getNewVarName());
                        break;
                    case VAR_ASSIGNMENT:
                        writer.println(instr.getType() + ";" + instr.getVarName() + ";" + instr.getAssignedValue());
                        break;
                    case INCREMENT_VARIABLE:

                        writer.println(instr.getType() + ";" + instr.getVarName() + ";" + instr.getDifference());
                        break;
                    case MOVE_FORWARD:
                    case TURN_LEFT:
                    case TURN_RIGHT:
                    case REPEAT:
                        // récupère soit le nombre, soit le nom de la variable (si c'est "nb" par ex) ou (ex: "50" ou "score")
                        String paramStr = (instr.getParameter() != null) ? instr.getParameter().toString() : "0";
                        writer.println(instr.getType() + ";" + paramStr);
                        break;
                    default:
                        // pour PEN_UP, PEN_DOWN, END_REPEAT (qui n'ont pas de paramètre)
                        // Pour les blocs simples sans texte (Stylo, Fin Répéter)
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
    // Il lit un fichier texte et recrée les blocs à l'écran
    public void loadFromFile(File file) {
        try (Scanner scanner = new Scanner(file)) {

            // 1. vide le programme et nettoie la mémoire des variables
            this.empty();
            declaredVariablesBeforeExec.clear();

            // 2. On lit le fichier ligne par ligne
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                if (ligne.trim().isEmpty()) continue; // ignore les lignes vides

                // On coupe la phrase à chaque point-virgule ";"
                String[] morceaux = ligne.split(";");
                String typeAction = morceaux[0]; // Le premier morceau c'est le nom du bloc

                // 3. On cherche dans le catalogue le bloc qui correspond à ce nom
                // crée la bonne instruction en fonction du texte
                for (int i = 0; i < catalogue.size(); i++) {
                    Instruction modele = catalogue.get(i);

                    if (modele.getType().toString().equals(typeAction)) {
                        // On a trouvé le bon bloc ! On l'ajoute à la liste du joueur
                        this.addInstruction(i, -1);
                        Instruction nouvelleAction = instructions.get(instructions.size() - 1);

                        // 4. S'il y avait du texte après le ";", on remplit les tiroirs du bloc
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

                                        nouvelleAction.setVarName(morceaux[1].trim());
                                        nouvelleAction.setAssignedValue(evaluate(morceaux[2].trim()));
                                    }
                                    break;
                                case INCREMENT_VARIABLE:
                                    if (morceaux.length >= 3) {

                                        nouvelleAction.setVarName(morceaux[1].trim());
                                        nouvelleAction.setDifference(evaluate(morceaux[2].trim()));
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

    // Le bouton "Vider tout"
    public void empty(){
        instructions.clear();
        variables.clear();
        declaredVariablesBeforeExec.clear();
    }

    // LECTURE DES LISTES PAR L'ÉCRAN

    // L'écran appelle ça pour dessiner le tiroir de gauche (catalogue)
    public static ObservableList<Instruction> getCatalogue() {
        // "unmodifiable" empêche l'écran de modifier le catalogue par erreur
        return FXCollections.unmodifiableObservableList(catalogue);
    }

    // L'écran appelle ça pour dessiner la zone de travail au milieu
    public static ObservableList<Instruction> getInstructions() {
        return FXCollections.unmodifiableObservableList(instructions);

    }

    // Quand le joueur tape du texte dans la case blanche d'un bloc
    public void changeAmount(Instruction instru, String param) {
        //si on veut declarer alors l'input est le nom de la nouvelle variable
        // CAS 1 : Le joueur tape dans le bloc "Déclaration variable"
        if(instru.getType() == InstructionType.VAR_DECLARATION){

            // On ne la crée que si elle n'existe pas déjà
            // On vérifie qu'il n'a pas déjà créé une variable avec ce nom
            if (!declaredVariablesBeforeExec.containsKey(param)) {
                // C'EST ICI QU'ON AJOUTE LA VARIABLE !
                declaredVariablesBeforeExec.put(param, new Variable(param));
                instru.setNewVarName(param);
            } else {
                // Si le nom existe déjà, on déclenche une erreur
                //isError = true;
                throw new IllegalArgumentException("Cette variable existe déjà.");
            }

        //sinon, on regarde si on donne à l'instru un entier -> on crée une value, si un string -> on crée une variable
            // CAS 2 : Le joueur tape dans n'importe quel autre bloc (ex: Avancer)
        } else {
            // On appelle notre traducteur pour savoir si c'est un chiffre ou une variable
            Value evaluated = evaluate(param);
            if (evaluated == null) {
                throw new RuntimeException("Valeur ou variable invalide");
            }
            // On donne la valeur traduite au bloc
            instru.setParameter(evaluated);
        }

        }




    // vérifie si le programme est correct (pas de PenActions en double + protection des variables de boucles)
    public boolean checkProgramValidity() {
        boolean state = true; // etat du pen sans execution

//        if(isError){
//            isError = false;
//            return false;
//        }

        // 1. La base : si une boucle n'est pas fermée, on bloque tout direct.
        if (!allLoopsClosed())
            return false;

        // on délimite la zone de déclaration de variables
        // 2. Vérifier que les déclarations de variables sont TOUTES au début
        int indexFirstinstruction = 0;
        int i = 0;

        // On avance tant qu'on lit des "Déclarations"
        while(i < instructions.size() && instructions.get(i).getType() == InstructionType.VAR_DECLARATION ){
            Instruction current = instructions.get(i);
            if (current.getNewVarName() == null || current.getNewVarName().isBlank())
                return false; // Erreur : variable sans nom
            indexFirstinstruction = i + 1; // + 1 car on veut verifier apres les variables. On retient où commencent les vraies actions
            i++;
        }

        // 3. La Pile (Stack) pour protéger les compteurs de boucle
        // pour retenir les variables bloquées par les boucles actuelles
        Stack<String> protectedVariables = new Stack<>();

        // 4. On lit le reste du programme
        for(int j = indexFirstinstruction; j < instructions.size(); j++) {
            Instruction current = instructions.get(j);

            // Règle A : Si on trouve une déclaration ici, c'est interdit (elles doivent être en haut)
            if(current.getType() == InstructionType.VAR_DECLARATION){
                return false;
            }

            // Règle B : Interdit de baisser un stylo déjà baissé (ou lever un stylo levé)
            if((current.getType() == InstructionType.PEN_DOWN && state) ||
                    (current.getType() == InstructionType.PEN_UP && !state)){
                return false;
            } else if((current.getType() == InstructionType.PEN_DOWN && !state) ||
                    (current.getType() == InstructionType.PEN_UP && state)){
                state = !state;
            }

            // On utilise toString() et on regarde si la map la connait.
            // Règle C : Protéger les variables dans les boucles
            if (current.getType() == InstructionType.REPEAT) {
                Value param = current.getParameter();
                // Si le paramètre existe et que son nom est dans la liste des variables déclarées
                // Exemple : Si la boucle utilise une variable (ex: "nb"), on la met dans la pile !
                if (param != null && declaredVariablesBeforeExec.containsKey(param.toString())) {
                    protectedVariables.push(param.toString());
                } else {
                    protectedVariables.push(""); // Ce n'est pas une variable. Boucle normale (ex: Répéter 10), rien à protéger
                }
            }
            else if (current.getType() == InstructionType.END_REPEAT) {
                // Fin de la boucle : on retire la variable de la pile (elle n'est plus protégée)
                if (!protectedVariables.isEmpty()) {
                    protectedVariables.pop();
                }
            }

            // Règle D : Interdit de modifier une variable protégée !
            // On utilise le polymorphisme avec getVarName()
            if (current.getType() == InstructionType.VAR_ASSIGNMENT ||
                    current.getType() == InstructionType.INCREMENT_VARIABLE) {

                String varModified = current.getVarName();
                // Si la pile contient le nom de cette variable
                if (protectedVariables.contains(varModified) || current.getVarName() == null || current.getVarName().isBlank()) {
                    // On essaie de modifier un compteur de boucle : PROGRAMME INVALIDE !
                    return false;
                }
            }
        }

        // Si le code a survécu à tous ces tests, il est valide !
        return true;
    }

    // Remet la tortue au centre
    public void reset() {
        this.pen.reset();
        this.penDown = true;
    }

    // traduit un texte en value
    public Value evaluate(String param) {
        // 1. Si la case est vide, on annule
        if (param == null || param.trim().isEmpty()) return null;
        try {
            // 2. essaie de voir si c'est un simple nombre. Essaie de forcer le texte à devenir un nombre entier (ex: "50" -> 50)
            return new Value(Integer.parseInt(param.trim()));
        } catch (NumberFormatException e) {
            // 3. Si ça plante (l'erreur NumberFormatException), ça veut dire que ce n'est pas un nombre.
            // Donc c'est sûrement le nom d'une variable (ex: "nb").
            // On regarde dans la mémoire (la Map) si on a bien une variable qui s'appelle comme ça.
            if (declaredVariablesBeforeExec.containsKey(param.trim())) {
                return declaredVariablesBeforeExec.get(param.trim());
            }

            // Si ce n'est ni un nombre, ni une variable connue, on renvoie null (erreur)
            return null;
        }
    }

    /*
    On vérifie si la variable qu'on veut donner en paramètre à une instruction a déjà été déclarée dans la zone de déclaration
    sans devoir passer par l'exécution du programme (vu que la variable n'est rééllement ajoutée au map qu'à l'exécution)
     */

    // Permet de vérifier rapidement si une variable existe dans la zone de préparation
    public Variable getVariableDeclared(String name) {
        return declaredVariablesBeforeExec.get(name);
    }

    // pour le bloc "Assigner" (ex: Mettre "score" à 10)
    public void assignVariable(Instruction instru, String variable, String valueText){
        // 1. On vérifie que la variable existe bien
            Variable var = declaredVariablesBeforeExec.get(variable);
            if (var == null) {
                throw new RuntimeException("Variable non déclarée");
            }

        // 2. On traduit le texte tapé par le joueur (ex: "10" devient le chiffre 10)
            Value evaluated = evaluate(valueText);
            if (evaluated == null) {
                throw new RuntimeException("Valeur invalide");
            }

        // 3. Si tout est bon, on donne l'ordre au bloc de s'actualiser
            if (instru.getType() == InstructionType.VAR_ASSIGNMENT) {
                instru.setParameter(evaluated);
                instru.assign(var, evaluated);
            }
        }

    // pour le bloc "Incrémenter/Décrémenter" (ex: Ajouter 5 à "score")
    public void incrementVariable(Instruction instru, String variable, String stepStr){
        // 1. On vérifie que la variable existe
        Variable var = declaredVariablesBeforeExec.get(variable);
        if (var == null) throw new RuntimeException("Variable non déclarée");

        // 2. On traduit la valeur tapée
        // On prend directement la valeur tapée dans la case
        Value stepVal = this.evaluate(stepStr);

        // 3. On donne l'ordre d'incrémenter
        if(instru.getType() == InstructionType.INCREMENT_VARIABLE){
            instru.increment(var, stepVal);
        }
        //((VarAssignment) instru).increment(var, stepVal);
    }

    // Vide le brouillon et remet les variables de base à zéro
    public void resetVariables() {
        // remet la valeur de chaque variable déclarée à 0
        for (Variable v : declaredVariablesBeforeExec.values()) {
            v.setValue(0);
        }
        // vide la map d'exece
        variables.clear();
    }

}
