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

        // Le titre au-dessus du tableau
        Label title = new Label("Variables");

        // Colonne 1 : Le nom de la variable
        TableColumn<Variable, String> nameCol = new TableColumn<>("Nom");
        // On dit à la colonne comment lire le nom dans l'objet Variable
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        // Colonne 2 : La valeur de la variable (le chiffre)
        TableColumn<Variable, Number> valueCol = new TableColumn<>("Valeur");
        valueCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()));

        // On ajoute les colonnes au tableau
        table.getColumns().addAll(nameCol, valueCol);

        // ON BRANCHE LE TALKIE-WALKIE ! Le tableau lira toujours la liste du Cerveau.
        table.setItems(scratchViewModel.getVariables());

        // Un peu de design
        table.setPrefHeight(150); // Hauteur du tableau
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); // étire les colonnes du tableau pour remplir la largeur

        // On ajoute le titre et le tableau dans la VBox
        this.getChildren().addAll(title, table);
        this.setSpacing(5);
    }
}