package view;

import controller.InscriptionController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Inscription;

import java.util.List;
import java.util.Map;

public class InscriptionView {

	private InscriptionController controller = new InscriptionController();

	private HBox filterBar = new HBox(12);
	private String selectedActivity = "Tous";

    public void show(Stage stage) {

    	
        Label title = new Label("Gestion des inscriptions");
        title.getStyleClass().add("page-title");

        Button statsBtn = new Button("Statistiques participants");
        statsBtn.getStyleClass().add("login-button");
        statsBtn.setOnAction(e -> afficherStatsParticipants());

        Button archiveBtn = new Button("Archiver tout");
        archiveBtn.getStyleClass().add("delete-button");
        archiveBtn.setOnAction(e -> confirmerArchivageTotal(stage));

        HBox topActions = new HBox(14, statsBtn, archiveBtn);
        topActions.setAlignment(Pos.CENTER_LEFT);
        HBox board = new HBox(25);
        board.setAlignment(Pos.TOP_LEFT);

        board.getChildren().addAll(
                createColumn("⏳ En attente", "EN_ATTENTE", stage),
                createColumn("✅ Acceptées", "ACCEPTEE", stage),
                createColumn("❌ Refusées", "REFUSEE", stage)
        );

        ScrollPane scroll = new ScrollPane(board);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("activity-scroll");

        createFilterBar(stage);

        VBox content = new VBox(25, title, topActions, filterBar, scroll);
        content.setPadding(new Insets(30));

        BorderPane root = new BorderPane();
        root.setTop(Navbar.create(stage, "inscriptions"));
        root.setCenter(content);
        root.getStyleClass().add("page-background");

        Scene scene = new Scene(root, 1400, 800);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        stage.setTitle("Gestion inscriptions - OmniSport");
        stage.setScene(scene);
        stage.show();
    }
    
    private void showArchiveBlockedPopup() {

        Stage popup = new Stage();

        popup.initModality(
                Modality.APPLICATION_MODAL
        );

        popup.initStyle(
                StageStyle.UNDECORATED
        );

        Label icon =
                new Label("🕔");

        icon.getStyleClass()
                .add("archive-clock");

        Label title =
                new Label(
                        "Archivage bloqué"
                );

        title.getStyleClass()
                .add("archive-title");

        Label text =
                new Label(
                        "L'archivage complet est disponible\n"
                        + "uniquement après 17:00."
                );

        text.setWrapText(true);

        text.getStyleClass()
                .add("archive-message");

        Button ok =
                new Button("Compris");

        ok.getStyleClass()
                .add("archive-btn");

        ok.setOnAction(
                e -> popup.close()
        );

        VBox card =
                new VBox(
                        18,
                        icon,
                        title,
                        text,
                        ok
                );

        card.setAlignment(
                Pos.CENTER
        );

        card.setPadding(
                new Insets(34)
        );

        card.getStyleClass()
                .add("archive-popup");

        Scene scene =
                new Scene(
                        card,
                        420,
                        280
                );

        scene.getStylesheets()
                .add(
                        getClass()
                        .getResource(
                                "/resources/style.css"
                        )
                        .toExternalForm()
                );

        popup.setScene(scene);

        popup.showAndWait();
    }
    
    private void confirmerArchivageTotal(Stage parentStage) {

        java.time.LocalTime now =
                java.time.LocalTime.now();

        java.time.LocalTime limit =
                java.time.LocalTime.of(17, 0);

        // ======================
        // BLOCK BEFORE 17:00
        // ======================

        if (now.isBefore(limit)) {

            Alert blocked =
                    new Alert(
                            Alert.AlertType.WARNING
                    );

            blocked.setTitle(
                    "Archivage bloqué"
            );

            blocked.setHeaderText(
                    null
            );

            blocked.setContentText(
            			    "Archivage disponible\n"
            			    + "uniquement après\n"
            			    + "17:00 ⏳"
            		
            );

            showArchiveBlockedPopup();
            return;

        }

        // ======================
        // CONFIRMATION
        // ======================

        Alert confirm =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirm.setTitle(
                "Archivage"
        );

        confirm.setHeaderText(
                "Archiver toutes les inscriptions ?"
        );

        confirm.setContentText(
                "Toutes les inscriptions seront déplacées vers archive_inscriptions."
        );

        if (
                confirm
                .showAndWait()
                .orElse(ButtonType.CANCEL)

                == ButtonType.OK
        ) {

            boolean success =
                    controller
                    .archiverToutesInscriptions();

            Alert result =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            result.setHeaderText(null);

            if (success) {

                result.setContentText(
                        "Archivage terminé ✅"
                );

                result.showAndWait();

                show(parentStage);

            } else {

                result.setContentText(
                        "Erreur ❌"
                );

                result.showAndWait();
            }

        }

    }
    
    private void createFilterBar(Stage stage) {

        filterBar.getChildren().clear();
        filterBar.setAlignment(Pos.CENTER_LEFT);

        Button allBtn = new Button("Tous");
        allBtn.getStyleClass().add("sport-filter-active");

        allBtn.setOnAction(e -> {
            selectedActivity = "Tous";
            show(stage);
        });

        filterBar.getChildren().add(allBtn);

        for (Inscription inscription : controller.getAllInscriptions()) {

            String sport = inscription.getActiviteNom();

            boolean exists = filterBar.getChildren()
                    .stream()
                    .filter(node -> node instanceof Button)
                    .map(node -> ((Button) node).getText())
                    .anyMatch(text -> text.equals(sport));

            if (!exists) {

                Button btn = new Button(sport);

                if (sport.equals(selectedActivity)) {
                    btn.getStyleClass().add("sport-filter-active");
                } else {
                    btn.getStyleClass().add("sport-filter-button");
                }

                btn.setOnAction(e -> {
                    selectedActivity = sport;
                    show(stage);
                });

                filterBar.getChildren().add(btn);
            }
        }
    }

    private VBox createColumn(String title, String statut, Stage stage) {

        VBox column = new VBox(16);
        column.getStyleClass().add("inscription-column");

        Label columnTitle = new Label(title);
        columnTitle.getStyleClass().add("inscription-column-title");

        VBox cards = new VBox(14);

        List<Inscription> inscriptions = controller.getAllInscriptions();

        for (Inscription inscription : inscriptions) {

            if (
                    inscription.getStatut().equals(statut)
                    &&
                    (
                            selectedActivity.equals("Tous")
                            ||
                            inscription.getActiviteNom()
                                    .equals(selectedActivity)
                    )
            ) {

                cards.getChildren()
                        .add(
                                createCard(
                                        inscription,
                                        stage
                                )
                        );
            }
        }

        if (cards.getChildren().isEmpty()) {
            Label empty = new Label("Aucune inscription");
            empty.getStyleClass().add("member-empty-text");
            cards.getChildren().add(empty);
        }

        column.getChildren().addAll(columnTitle, cards);

        return column;
    }

    private VBox createCard(Inscription inscription, Stage stage) {

        Label membre = new Label("👤 " + inscription.getMembreNom());
        membre.getStyleClass().add("inscription-card-title");

        Label activite = new Label("🏋 " + inscription.getActiviteNom());
        activite.getStyleClass().add("inscription-card-info");

        Label statut = new Label(formatStatut(inscription.getStatut()));
        statut.getStyleClass().add(getStatutClass(inscription.getStatut()));

        Button accepterBtn = new Button("Accepter");
        accepterBtn.getStyleClass().add("accept-button");

        Button refuserBtn = new Button("Refuser");
        refuserBtn.getStyleClass().add("refuse-button");

        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.getStyleClass().add("delete-button");

        accepterBtn.setOnAction(e -> {
            if (controller.accepterInscription(inscription.getId())) {
                show(stage);
            }
        });

        refuserBtn.setOnAction(e -> {
            if (controller.refuserInscription(inscription.getId())) {
                show(stage);
            }
        });

        supprimerBtn.setOnAction(e -> afficherConfirmationSuppression(inscription, stage));

        HBox actions = new HBox(10);

        if (inscription.getStatut().equals("EN_ATTENTE")) {
            actions.getChildren().addAll(accepterBtn, refuserBtn, supprimerBtn);
        } else if (inscription.getStatut().equals("ACCEPTEE")) {
            actions.getChildren().addAll(refuserBtn, supprimerBtn);
        } else {
            actions.getChildren().addAll(accepterBtn, supprimerBtn);
        }

        VBox card = new VBox(12, membre, activite, statut, actions);
        card.getStyleClass().add("inscription-card");

        return card;
    }

    private String formatStatut(String statut) {

        if (statut.equals("EN_ATTENTE")) {
            return "⏳ En attente";
        }

        if (statut.equals("ACCEPTEE")) {
            return "✅ Acceptée";
        }

        if (statut.equals("REFUSEE")) {
            return "❌ Refusée";
        }

        return statut;
    }

    private String getStatutClass(String statut) {

        if (statut.equals("EN_ATTENTE")) {
            return "status-waiting";
        }

        if (statut.equals("ACCEPTEE")) {
            return "status-accepted";
        }

        return "status-refused";
    }

    private void afficherConfirmationSuppression(Inscription inscription, Stage parentStage) {

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initStyle(StageStyle.UNDECORATED);

        Label icon = new Label("⚠");
        icon.getStyleClass().add("warning-icon");

        Label title = new Label("Supprimer cette inscription ?");
        title.getStyleClass().add("popup-title");

        Label text = new Label(
                "Voulez-vous vraiment supprimer l'inscription de "
                        + inscription.getMembreNom()
                        + " ?"
        );
        text.getStyleClass().add("popup-message");

        Button annulerBtn = new Button("Annuler");
        annulerBtn.getStyleClass().add("cancel-button");

        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.getStyleClass().add("delete-button");

        annulerBtn.setOnAction(e -> popup.close());

        supprimerBtn.setOnAction(e -> {
            if (controller.supprimerInscription(inscription.getId())) {
                popup.close();
                show(parentStage);
            }
        });

        HBox buttons = new HBox(15, annulerBtn, supprimerBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(18, icon, title, text, buttons);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("confirm-box");

        Scene scene = new Scene(root, 460, 260);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        popup.setScene(scene);
        popup.showAndWait();
    }

    private void afficherStatsParticipants() {

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Participants par activité");

        Label title = new Label("Nombre de participants par activité");
        title.getStyleClass().add("page-title");

        PieChart chart = new PieChart();

        Map<String, Integer> stats = controller.getParticipantsParActivite();

        for (String activite : stats.keySet()) {
            chart.getData().add(
                    new PieChart.Data(
                            activite + " (" + stats.get(activite) + ")",
                            stats.get(activite)
                    )
            );
        }

        chart.setTitle("Répartition des participants acceptés");
        chart.setLabelsVisible(true);
        chart.setLegendVisible(true);

        VBox root = new VBox(20, title, chart);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("page-background");

        Scene scene = new Scene(root, 700, 600);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        popup.setScene(scene);
        popup.showAndWait();
    }
}