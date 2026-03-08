package scratch.view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import scratch.viewmodel.MainViewModel;
import scratch.viewmodel.ScratchViewModel;

import java.io.File;

public class MainView extends BorderPane {
    private MainViewModel mainViewModel;

    public MainView(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem loadItem = new MenuItem("Charger un programme");
        MenuItem saveItem = new MenuItem("Sauvegarder le programme");
        fileMenu.getItems().addAll(loadItem, saveItem);
        menuBar.getMenus().add(fileMenu);


        saveItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder le programme");
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            mainViewModel.saveProgram(file);
        });

        loadItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Charger un programme");
            File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            mainViewModel.loadProgram(file);
        });

        this.setTop(menuBar);

        // récupère le ViewModel de la ScratchView et on l'instancie
        ScratchViewModel scratchVM = mainViewModel.getScratchViewModel();

        ScratchView scratchView = new ScratchView(scratchVM);

        this.setCenter(scratchView);
    }
}
