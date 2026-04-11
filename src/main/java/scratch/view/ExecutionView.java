package scratch.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import scratch.viewmodel.ExecutionViewModel;
import scratch.viewmodel.ScratchViewModel;

public class ExecutionView extends VBox {

    public ExecutionView(ScratchViewModel scratchVM, Runnable onResetCanvas) {
        ExecutionViewModel execVM = scratchVM.getExecutionVM();

        // Boutons radio
        RadioButton manualRadio = new RadioButton("Exécution manuelle");
        RadioButton autoRadio = new RadioButton("Exécution automatique");
        ToggleGroup group = new ToggleGroup();
        manualRadio.setToggleGroup(group);
        autoRadio.setToggleGroup(group);
        manualRadio.setSelected(true);

        execVM.isAutoModeProperty().bind(autoRadio.selectedProperty());

        HBox radioBox = new HBox(15, autoRadio, manualRadio);
        radioBox.setAlignment(Pos.CENTER);

        // Boutons d'action
        Button reinitButton = new Button("Ré-initialiser");
        Button nextButton = new Button("Suivant");
        Button stopButton = new Button("Arrêter");

        // Binding des textes et désactivations
        reinitButton.textProperty().bind(scratchVM.leftBtnTextProperty());
        reinitButton.disableProperty().bind(scratchVM.leftBtnDisabledProperty());

        nextButton.textProperty().bind(scratchVM.rightBtnTextProperty());
        nextButton.disableProperty().bind(scratchVM.rightBtnDisabledProperty());

        stopButton.disableProperty().bind(execVM.stopBtnDisabledProperty());
        stopButton.visibleProperty().bind(execVM.isAutoModeProperty());
        stopButton.managedProperty().bind(execVM.isAutoModeProperty());

        // Actions des boutons
        reinitButton.setOnAction(e -> {
            scratchVM.clickLeftButton();
            if (onResetCanvas != null) onResetCanvas.run(); // Nettoie le canvas de ScratchView
        });

        nextButton.setOnAction(e -> scratchVM.clickRightButton());
        stopButton.setOnAction(e -> execVM.stopAuto());

        HBox buttonBox = new HBox(10, reinitButton, nextButton, stopButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Slider de vitesse
        Slider speedSlider = new Slider(0.01, 5, 1);
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);
        speedSlider.setMajorTickUnit(1);

        speedSlider.valueProperty().bindBidirectional(execVM.speedDelayProperty());
        speedSlider.visibleProperty().bind(execVM.isAutoModeProperty());
        speedSlider.managedProperty().bind(execVM.isAutoModeProperty());

        // Assemblage de l'ExecutionView
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(radioBox, buttonBox, speedSlider);
    }
}