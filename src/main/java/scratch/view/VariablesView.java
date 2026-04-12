package scratch.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Variable, Number> valueCol = new TableColumn<>("Valeur");

        valueCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()));

        table.getColumns().addAll(nameCol, valueCol);

        table.setItems(scratchViewModel.getVariables());

        table.setPrefHeight(150);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        this.getChildren().addAll(title, table);
        this.setSpacing(5);
    }
}