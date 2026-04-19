package scratch.viewmodel;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.util.Duration;

public class ExecutionViewModel {

    // Le lien vers le cerveau principal
    private final ScratchViewModel scratchVM;

    private final BooleanProperty isAutoMode = new SimpleBooleanProperty(false); // Le mode auto est-il coché ?
    private final DoubleProperty speedDelay = new SimpleDoubleProperty(1.0); // La vitesse (1.0 = 1 seconde)
    private final BooleanProperty stopBtnDisabled = new SimpleBooleanProperty(true); // Le bouton "Arrêter" est-il grisé ?

    // Le chronomètre de JavaFX (le métronome)
    private Timeline timeline;

    public ExecutionViewModel(ScratchViewModel scratchVM) {
        this.scratchVM = scratchVM;

        // 1. L'espion de la vitesse : Si le joueur bouge le curseur de vitesse PENDANT l'animation
        // adapte la vitesse en direct si le slider bouge
        speedDelay.addListener((obs, oldVal, newVal) -> {
            if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
                playAuto();
            }
        });

        // 2. L'espion du mode Auto : Si on décoche la case "Mode Auto", on arrête tout
        // si on décoche le mode auto, on arrête l'horloge
        isAutoMode.addListener((obs, oldVal, newVal) -> {
            if (!newVal) stopAuto();
        });
    }

    // LES PONTS POUR BRANCHER L'ÉCRAN
    public BooleanProperty isAutoModeProperty() { return isAutoMode; }
    public DoubleProperty speedDelayProperty() { return speedDelay; }
    public BooleanProperty stopBtnDisabledProperty() { return stopBtnDisabled; }

    // --- LES ACTIONS DES BOUTONS ---

    // Lancer la lecture automatique (Le bouton "Play")
    public void playAuto() {
        // S'il y avait déjà un chrono, on l'arrête pour en recréer un proprement
        if (timeline != null) timeline.stop();

        // On crée un nouveau chrono réglé sur la vitesse actuelle (speedDelay)
        timeline = new Timeline(new KeyFrame(Duration.seconds(speedDelay.get()), e -> {
            // À chaque "Tic" de l'horloge, on demande au cerveau de lire la ligne suivante !
            // On demande au ScratchViewModel de faire une étape
            boolean canContinue = scratchVM.executeStep();

            // Si le cerveau dit "J'ai fini le programme" ou "Erreur", on arrête le chrono
            if (!canContinue) {
                stopAuto();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // L'horloge doit tourner à l'infini
        timeline.play(); // DÉMARRAGE !

        stopBtnDisabled.set(false); // On allume le bouton Stop
        scratchVM.rightBtnDisabledProperty().set(true); // bloque le bouton Suivant/Exécuter
    }

    // Arrêter la lecture automatique (Le bouton "Stop")
    public void stopAuto() {
        if (timeline != null) timeline.stop(); // On coupe le chrono

        stopBtnDisabled.set(true); // On grise le bouton Stop
        scratchVM.updateState(); // demande au ScratchViewModel de remettre ses boutons dans le bon état
    }
}