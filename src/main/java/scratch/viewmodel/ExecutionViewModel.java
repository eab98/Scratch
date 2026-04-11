package scratch.viewmodel;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.util.Duration;

public class ExecutionViewModel {
    private final ScratchViewModel scratchVM;

    private final BooleanProperty isAutoMode = new SimpleBooleanProperty(false);
    private final DoubleProperty speedDelay = new SimpleDoubleProperty(1.0);
    private final BooleanProperty stopBtnDisabled = new SimpleBooleanProperty(true);
    private Timeline timeline;

    public ExecutionViewModel(ScratchViewModel scratchVM) {
        this.scratchVM = scratchVM;

        // adapte la vitesse en direct si le slider bouge
        speedDelay.addListener((obs, oldVal, newVal) -> {
            if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
                playAuto();
            }
        });

        // si on décoche le mode auto, on arrête l'horloge
        isAutoMode.addListener((obs, oldVal, newVal) -> {
            if (!newVal) stopAuto();
        });
    }

    public BooleanProperty isAutoModeProperty() { return isAutoMode; }
    public DoubleProperty speedDelayProperty() { return speedDelay; }
    public BooleanProperty stopBtnDisabledProperty() { return stopBtnDisabled; }

    public void playAuto() {
        if (timeline != null) timeline.stop();

        timeline = new Timeline(new KeyFrame(Duration.seconds(speedDelay.get()), e -> {
            // On demande au ScratchViewModel de faire une étape
            boolean canContinue = scratchVM.executeStep();
            if (!canContinue) {
                stopAuto();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        stopBtnDisabled.set(false);
        scratchVM.rightBtnDisabledProperty().set(true); // bloque le bouton Suivant/Exécuter
    }

    public void stopAuto() {
        if (timeline != null) timeline.stop();
        stopBtnDisabled.set(true);
        scratchVM.updateState(); // demande au ScratchViewModel de remettre ses boutons dans le bon état
    }
}