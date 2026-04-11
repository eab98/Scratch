package scratch.view;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import scratch.model.Variable;
import scratch.viewmodel.ScratchViewModel;

public class VariablesView extends VBox {

    private final ScratchViewModel scratchViewModel;
    private final TableView<Variable> table = new TableView<>();

    public VariablesView(ScratchViewModel scratchViewModel) {
        this.scratchViewModel = scratchViewModel;

        Label title = new Label("Variables");

        TableColumn<Variable, String> nameCol = new TableColumn<>("Nom");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Variable, Integer> valueCol = new TableColumn<>("Valeur");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        table.getColumns().addAll(nameCol, valueCol);

        table.setItems(scratchViewModel.getVariables());

        table.setPrefHeight(150);
        // adapte la largeur des colonnes
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        this.getChildren().addAll(title, table);
        this.setSpacing(5);
    }
}
