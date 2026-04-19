package scratch.view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import scratch.viewmodel.MainViewModel;
import scratch.viewmodel.ScratchViewModel;

import java.io.File;

// BorderPane = Un conteneur avec 5 zones (Haut, Bas, Gauche, Droite, Centre)
public class MainView extends BorderPane {
    private MainViewModel mainViewModel;

    public MainView(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;

        // --- Creation du menu du haut ---
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem loadItem = new MenuItem("Charger un programme");
        MenuItem saveItem = new MenuItem("Sauvegarder le programme");
        fileMenu.getItems().addAll(loadItem, saveItem);
        menuBar.getMenus().add(fileMenu);

        // Action : Quand on clique sur "Sauvegarder"
        saveItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder le programme");
            File file = fileChooser.showSaveDialog(this.getScene().getWindow()); // Ouvre la fenêtre Windows/Mac classique
            mainViewModel.saveProgram(file); // On donne l'ordre au grand patron
        });

        // Action : Quand on clique sur "Charger"
        loadItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Charger un programme");
            File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            mainViewModel.loadProgram(file);
        });

        // On place le menu tout en haut (Top)
        this.setTop(menuBar);

        // récupère le ViewModel de la ScratchView et on l'instancie
        ScratchViewModel scratchVM = mainViewModel.getScratchViewModel();

        // On crée la grosse interface du jeu
        ScratchView scratchView = new ScratchView(scratchVM);

        // On la place au centre
        this.setCenter(scratchView);
    }
}
