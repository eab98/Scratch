package scratch.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import scratch.model.Instruction;
import scratch.viewmodel.ScratchViewModel;

import javax.swing.*;


public class ScratchView extends BorderPane {

    private static final double TURTLE_HEAD_SIZE = 5;
    private ScratchViewModel scratchViewModel;

    private final VBox palette = new VBox(8);
    private final VBox program = new VBox(8);
    private final VBox scene = new VBox(20);

    private final Group headTurtle = new Group();

    private final Label paletteTitle = new Label("Palette d'actions");
    private final Label programTitle = new Label("Programme");
    private final Label sceneTitle = new Label("Scène");
    
    private final HBox buttonProgram = new HBox(8);
    private final HBox buttonScene = new HBox(10);

    private final ListView<Instruction> paletteList = new ListView<>();
    private final ListView<Instruction> programList = new ListView<>();

    private final Canvas canvas = new Canvas(400, 400);

    private final Button addToProgramButton = new Button("Ajouter au programme");

    private final Button upButton = new Button("Monter");
    private final Button downButton = new Button("Descendre");
    private final Button duplicateButton = new Button("Dupliquer");
    private final Button deleteButton = new Button("Supprimer");
    private final Button emptyButton = new Button("Vider tout");

    private final Button reinitButton = new Button("Ré-initiliaser");
    private final Button nextButton = new Button("Suivant");

    private final TitledPane detailsAction = new TitledPane();
    private final HBox content = new HBox();
    private final VBox contentPlusError = new VBox();
    private final TextField input = new TextField();
    private final Label errorMessage = new Label();

    //on initialise le listener ici pour pouvoir l'ajouter/l'enlever plus facilement
    private ChangeListener<String> inputListener;


    public ScratchView(ScratchViewModel scratchViewModel) {
        this.scratchViewModel = scratchViewModel;
        configPaletteBox();
        configProgramBox();
        configSceneBox();

        this.setLeft(palette);
        this.setCenter(program);
        this.setRight(scene);

        loadData();
        configSelection();
        configActions();
        configButtonsDisabling();
        beautify();

    }
    
    private void configPaletteBox() {
        palette.getChildren().addAll(paletteTitle, paletteList, addToProgramButton);
    }
    
    private void configButtonsProg() {
        buttonProgram.getChildren().addAll(upButton, downButton, duplicateButton, deleteButton, emptyButton);
    }

    private void configProgramBox(){
        configButtonsProg();
        program.getChildren().addAll(programTitle, programList, buttonProgram);
        detailsAction.setText("Détails de l'action");
        program.getChildren().add(detailsAction);

        errorMessage.setTextFill(Color.RED);
        updateDetails();
        configInputAmount();
    }

    private void configSceneBox() {
        configButtonsScene();

        Pane canvasContainer = new Pane();
        canvasContainer.getChildren().addAll(canvas, headTurtle);

        scene.getChildren().addAll(sceneTitle, canvasContainer, buttonScene);
        configCanvas();
    }

    private void configButtonsScene() {
        buttonScene.getChildren().addAll(reinitButton, nextButton);

        reinitButton.textProperty().bind(scratchViewModel.leftBtnTextProperty());
        reinitButton.disableProperty().bind(scratchViewModel.leftBtnDisabledProperty());

        nextButton.textProperty().bind(scratchViewModel.rightBtnTextProperty());
        nextButton.disableProperty().bind(scratchViewModel.rightBtnDisabledProperty());
    }

    private void configSelection() {
        this.paletteList.getSelectionModel().selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> {
                    this.scratchViewModel.getCatalogIndexProperty().setValue(newValue.intValue());
                } );
        this.programList.getSelectionModel().selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> {
                    this.scratchViewModel.getIndexProperty().setValue(newValue.intValue());
                    updateDetails();
                } );
        this.scratchViewModel.getIndexProperty().addListener((observable, oldValue, newValue) -> {
            this.programList.getSelectionModel().select(newValue.intValue());
        });

    }

    private void configCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);

        double w = canvas.getWidth();
        double h = canvas.getHeight();

        gc.fillRect(0, 0, w, h);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.2);

        for (int i = 15; i < w; i += 30) {
            gc.strokeLine(i, 0, i, h);
        }

        gc.setStroke(Color.BLACK);

        for (int i = 15; i < h; i += 30) {
            gc.strokeLine(0, i, w, i);
        }

        // le triangle bleu
        Polygon body = new Polygon(
                -7.5, -5.0,
                7.5, 0.0,
                -7.5, 5.0
        );
        body.setFill(Color.DEEPSKYBLUE);
        body.setStroke(Color.BLACK);

        // le petit point vert
        Circle dot = new Circle(7.5, 0.0, 2.5);
        dot.setFill(Color.GREEN);
        dot.setStroke(Color.BLACK);

        // assemblage dans le groupe
        headTurtle.getChildren().addAll(body, dot);

        // coordonnée au centre
        headTurtle.setLayoutX(195);
        headTurtle.setLayoutY(195);

        // angle initial au demarrage
        headTurtle.setRotate(scratchViewModel.getOrientation());
    }

    private void configActions(){
        addToProgramButton.setOnAction(e -> scratchViewModel.addInstruction());
        this.paletteList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2)
                scratchViewModel.addInstruction();
        });
        upButton.setOnAction(e -> scratchViewModel.up());
        downButton.setOnAction(e -> scratchViewModel.down());
        duplicateButton.setOnAction(e-> scratchViewModel.duplicate());
        deleteButton.setOnAction(e-> scratchViewModel.removeInstru());
        emptyButton.setOnAction(e-> scratchViewModel.empty());
        nextButton.setOnAction(e -> {
            scratchViewModel.clickRightButton();
            updateCanvas();
        });
        reinitButton.setOnAction(e -> {
            scratchViewModel.clickLeftButton();
            //efface le canvas
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            // redessine la grille
            configCanvas();
        });
    }

    private void configButtonsDisabling() {
        upButton.disableProperty().bind(scratchViewModel.canUp().not());
        downButton.disableProperty().bind(scratchViewModel.canDown().not());
        duplicateButton.disableProperty().bind(scratchViewModel.canDuplicateOrDelete().not());
        deleteButton.disableProperty().bind(scratchViewModel.canDuplicateOrDelete().not());
        emptyButton.disableProperty().bind(scratchViewModel.canEmpty().not());
    }

    private void configInputAmount() {
        errorMessage.textProperty().bind(scratchViewModel.getErrorMessage());
        inputListener = (observable, oldValue, newValue)
              -> {

            /*besoin du try/catch car si on vide l'input box et qu'on a donc un String vide, on a une erreur de type
            NumberFormatException qui s'affiche dans la console */

            try{
                int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
                this.scratchViewModel.getAmountProperty().set(value);
                this.scratchViewModel.changeAmount();
            }catch(NumberFormatException _){

            }

        };
        input.textProperty().addListener(inputListener);
        input.setOnAction(e -> {
            this.programList.refresh();
        });
    }

    private void loadData() {
        paletteList.setItems(scratchViewModel.getCatalogue());
        programList.setItems(scratchViewModel.getInstru());
    }

    private void beautify() {
        palette.setPrefWidth(250);
        scene.setPrefWidth(500);
        program.setPrefWidth(300);

        BorderPane.setAlignment(palette, Pos.CENTER_LEFT);
        BorderPane.setAlignment(program, Pos.CENTER);
        BorderPane.setAlignment(scene, Pos.CENTER_RIGHT);

        this.setPadding(new Insets(2));
        this.program.setPadding(new Insets(2));
        this.palette.setPadding(new Insets(2));
        this.scene.setPadding(new Insets(2));

        BorderPane.setMargin(palette, new Insets(2));
        BorderPane.setMargin(program, new Insets(2));
        BorderPane.setMargin(scene, new Insets(2));

        input.setMaxWidth(50);
        input.setPrefColumnCount(3);
        input.setAlignment(Pos.CENTER);

        this.content.setSpacing(5);

    }


    private void updateCanvas() {
        // mémorise l'ancienne position de la tortue
        double oldX = headTurtle.getLayoutX();
        double oldY = headTurtle.getLayoutY();

        // récupère la nouvelle position calculée
        double newX = scratchViewModel.coordXProperty().get();
        double newY = scratchViewModel.coordYProperty().get();

        // dessine la ligne rouge (si pen baissé)
        if (scratchViewModel.isPenDown()) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokeLine(oldX, oldY, newX, newY);
        }

        // déplace la tortue à sa nouvelle position
        headTurtle.setLayoutX(newX);
        headTurtle.setLayoutY(newY);
        headTurtle.setRotate(scratchViewModel.getOrientation());
    }

    private void updateDetails(){
        int index = scratchViewModel.getIndexProperty().get();
        if(index == -1){
            Label empty = new Label("(aucune action selectionnée)");
            detailsAction.setContent(empty);

        }else {
            Label text = new Label(scratchViewModel.getNameInstruParIndex());

            //si l'instruction est un pen_up ou un pen_down le textfield doit etre desactive et pas d'erreur possible
            if (scratchViewModel.isPenAction()) {
                input.setText("0");
                input.setDisable(true);
                content.getChildren().clear();
                content.getChildren().addAll(text, input);
                detailsAction.setContent(content);
            }

            //pour les autres instructions
            else {
                Label text2 = new Label("pixels");

                int param = scratchViewModel.getParameterInstru();

                input.setDisable(false);

                //on enleve le listener, on attend que l'user introduise l'input, on remet le listener pour notifier le vm
                input.textProperty().removeListener(inputListener);
                input.setText(Integer.toString(param));
                input.textProperty().addListener(inputListener);
                content.getChildren().clear();
                content.getChildren().addAll(text, input, text2);

                contentPlusError.getChildren().clear();
                contentPlusError.getChildren().addAll(content, errorMessage);
                detailsAction.setContent(contentPlusError);
            }
        }
    }

}
