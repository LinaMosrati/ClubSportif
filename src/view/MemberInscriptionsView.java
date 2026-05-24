package view;

import controller.InscriptionController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Inscription;

public class MemberInscriptionsView {

    private TableView<Inscription> table = new TableView<>();
    private InscriptionController controller = new InscriptionController();

    public void show(Stage stage, String login) {

        Label title = new Label("Mes inscriptions");
        title.getStyleClass().add("page-title");

        TableColumn<Inscription, String> activiteCol = new TableColumn<>("Activité");
        activiteCol.setCellValueFactory(new PropertyValueFactory<>("activiteNom"));

        TableColumn<Inscription, String> horaireCol = new TableColumn<>("Horaire");
        horaireCol.setCellValueFactory(new PropertyValueFactory<>("horaire"));

        TableColumn<Inscription, Void> statutCol = new TableColumn<>("Statut");

        statutCol.setCellFactory(col -> new TableCell<>() {

            @Override
            protected void updateItem(Void item, boolean empty) {

                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    Inscription inscription =
                            getTableView().getItems().get(getIndex());

                    setGraphic(createStatutView(inscription.getStatut()));
                }
            }
        });

        TableColumn<Inscription, Void> actionCol = new TableColumn<>("Action");

        actionCol.setCellFactory(col -> new TableCell<>() {

            private final Button annulerBtn = new Button("Annuler");

            {
                annulerBtn.getStyleClass().add("delete-button");

                annulerBtn.setOnAction(e -> {

                    Alert confirm =
                            new Alert(
                                    Alert.AlertType.CONFIRMATION
                            );

                    confirm.setTitle("Annuler");

                    confirm.setHeaderText(
                            "Annuler inscription ?"
                    );

                    confirm.setContentText(
                            "Elle sera archivée."
                    );

                    if (
                            confirm
                            .showAndWait()
                            .orElse(ButtonType.CANCEL)

                            == ButtonType.OK
                    ) {

                        Inscription inscription =
                                getTableView()
                                .getItems()
                                .get(getIndex());

                        boolean success =
                                controller
                                .supprimerInscription(
                                        inscription.getId()
                                );

                        if (success) {

                            Alert ok =
                                    new Alert(
                                            Alert.AlertType.INFORMATION
                                    );

                            ok.setHeaderText(null);

                            ok.setContentText(
                                    "Inscription archivée ✅"
                            );

                            ok.show();

                            chargerInscriptions(login);
                        }

                    }

                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {

                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(annulerBtn);
                }
            }
        });

        table.getColumns().addAll(
                activiteCol,
                horaireCol,
                statutCol,
                actionCol
        );

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(460);

        chargerInscriptions(login);

        VBox content = new VBox(20, title, table);
        content.setPadding(new Insets(30));

        BorderPane root = new BorderPane();
        root.setTop(MemberNavbar.create(stage, "inscriptions", login));
        root.setCenter(content);
        root.getStyleClass().add("page-background");

        Scene scene = new Scene(root, 1400, 800);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        stage.setTitle("Mes inscriptions - OmniSport");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void chargerInscriptions(String login) {

        table.setItems(
                FXCollections.observableArrayList(
                        controller.getInscriptionsByLogin(login)
                )
        );
    }

    private HBox createStatutView(String statut) {

        Label attente = createStep("● En attente", statut.equals("EN_ATTENTE"));
        Label acceptee = createStep("● Acceptée", statut.equals("ACCEPTEE"));
        Label refusee = createStep("● Refusée", statut.equals("REFUSEE"));

        HBox box = new HBox(12, attente, acceptee, refusee);
        box.setAlignment(Pos.CENTER_LEFT);

        return box;
    }

    private Label createStep(String text, boolean active) {

        Label label = new Label(text);

        if (active) {
            label.getStyleClass().add("status-active");
        } else {
            label.getStyleClass().add("status-inactive");
        }

        return label;
    }
}