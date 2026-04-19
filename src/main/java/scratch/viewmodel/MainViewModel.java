package scratch.viewmodel;

import scratch.model.Program;

import java.io.File;

public class MainViewModel {

    // 1. Le "Moteur" du jeu
    private final Program program;

    // 2. Le "Cerveau" principal
    private final ScratchViewModel scratchViewModel;

    // Le constructeur : Quand on lance l'application, on connecte le moteur au cerveau
    public MainViewModel(Program program) {
        this.program = program;
        this.scratchViewModel = new ScratchViewModel(program);
    }

    // Permet à l'écran de récupérer le cerveau pour pouvoir lui parler
    public ScratchViewModel getScratchViewModel() {
        return scratchViewModel;
    }

    // --- GESTION DES SAUVEGARDES ---

    // Quand le joueur clique sur "Fichier > Enregistrer"
    public void saveProgram(File file) {
        if (file != null) {
            program.saveToFile(file); // On demande au moteur d'écrire le fichier sur l'ordinateur
        }
    }

    // Quand le joueur clique sur "Fichier > Ouvrir"
    public void loadProgram(File file) {
        if (file != null) {
            program.loadFromFile(file); // On demande au moteur de lire le fichier
            scratchViewModel.notifyProgramChanged(); // On alerte le cerveau que le code a changé pour qu'il mette l'écran à jour !
        }
    }
}
