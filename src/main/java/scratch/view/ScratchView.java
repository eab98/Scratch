package scratch.view;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import scratch.model.Instruction;
import scratch.viewmodel.MainViewModel;
import scratch.viewmodel.ScratchViewModel;



public class ScratchView extends BorderPane {

    private static final double TURTLE_HEAD_SIZE = 5;
    private ScratchViewModel scratchViewModel;

    private final VBox palette = new VBox(10);
    private final VBox program = new VBox(10);
    private final VBox scene = new VBox(20);

    private final Circle headTurtle = new Circle(TURTLE_HEAD_SIZE);


    private final Label paletteTitle = new Label("Palette d'actions");
    private final Label programTitle = new Label("Programme");
    private final Label sceneTitle = new Label("Scène");
    
    private final HBox buttonProgram = new HBox(10);
    private final HBox buttonScene = new HBox(10);

    private final ListView<Instruction> paletteList = new ListView<>();
    private final ListView<Instruction> programList = new ListView<>();

    private final Canvas canvas = new Canvas(300, 300);

    private final Button addToProgramButton = new Button("Ajouter au programme");

    private final Button upButton = new Button("Monter");
    private final Button downButton = new Button("Descendre");
    private final Button duplicateButton = new Button("Dupliquer");
    private final Button deleteButton = new Button("Supprimer");
    private final Button emptyButton = new Button("Vider tout");

    private final Button reinitButton = new Button("Ré-initiliaser");
    private final Button nextButton = new Button("Suivant");
    
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
    }

    private void configSceneBox() {
        configButtonsScene();

        StackPane canvasContainer = new StackPane();
        canvasContainer.getChildren().addAll(canvas, headTurtle);

        scene.getChildren().addAll(sceneTitle, canvasContainer, buttonScene);
        configCanvas();
    }

    private void configButtonsScene() {
        buttonScene.getChildren().addAll(reinitButton, nextButton);
    }

    private void configSelection() {
        this.paletteList.getSelectionModel().selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> {
                    this.scratchViewModel.getCatalogIndexProperty().setValue(newValue.intValue());
                } );
        this.programList.getSelectionModel().selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> {
                    this.scratchViewModel.getIndexProperty().setValue(newValue.intValue());
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

        System.out.println(getWidth() + ", " + getHeight());

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.2);

        for (int i = 15; i < w; i += 30) {
            gc.strokeLine(i, 0, i, h);
        }

        gc.setStroke(Color.BLACK);

        for (int i = 15; i < h; i += 30) {
            gc.strokeLine(0, i, w, i);
        }

        headTurtle.setCenterX(w / 2);
        headTurtle.setCenterY(h / 2);

    }

    private void configActions(){
        addToProgramButton.setOnAction(e -> scratchViewModel.addInstruction());
        upButton.setOnAction(e -> scratchViewModel.up());
        downButton.setOnAction(e -> scratchViewModel.down());
        duplicateButton.setOnAction(e-> scratchViewModel.duplicate());
        deleteButton.setOnAction(e-> scratchViewModel.removeInstru());
        emptyButton.setOnAction(e-> scratchViewModel.empty());
    }

    private void loadData() {
        paletteList.setItems(scratchViewModel.getCatalogue());
        programList.setItems(scratchViewModel.getInstru());
    }

    private void beautify() {
        palette.setPrefWidth(50);
        scene.setPrefWidth(60);

        this.setPadding(new Insets(100));
        this.program.setPadding(new Insets(16, 16, 12, 16));
        this.palette.setPadding(new Insets(16, 16, 12, 16));
        this.scene.setPadding(new Insets(16, 16, 12, 16));
        this.programList.setPadding(new Insets(16, 16, 12, 16));
        this.paletteList.setPadding(new Insets(16, 16, 12, 16));

        BorderPane.setMargin(palette, new Insets(10));
        BorderPane.setMargin(program, new Insets(10));
        BorderPane.setMargin(scene, new Insets(10));

    }

}
