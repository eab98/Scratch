package scratch;

import scratch.model.Character;
import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import scratch.model.Coordinates;
import scratch.model.Program;
import scratch.view.MainView;
import scratch.view.ScratchView;
import scratch.viewmodel.MainViewModel;
import scratch.viewmodel.ScratchViewModel;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
//        Label label = new Label("Hello ANC4 2526");
//        label.setFont(new Font(25));
//        Pane pane = new StackPane();
//        pane.getChildren().add(label);
//        Scene scene = new Scene(pane, 640, 480);
//        primaryStage.setTitle("Scratch");
//        primaryStage.setScene(scene);

        Coordinates origin = new Coordinates(200, 200);
        Character pen = new Character(0, origin);
        Program program = new Program(pen);

        MainViewModel mainViewModel = new MainViewModel(program);

        MainView mainView = new MainView(mainViewModel);

        Scene scene = new Scene(mainView, 800, 600);
        primaryStage.setTitle("Scratch");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}