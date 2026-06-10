package view;

import controller.ActiviteController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import controller.InscriptionController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Activite;
import java.util.ArrayList;
import java.util.List;

public class MemberActivitiesView {

    private GridPane planningGrid = new GridPane();
    private ActiviteController activiteController = new ActiviteController();
    private InscriptionController inscriptionController = new InscriptionController();

    private final String[] jours = {
            "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"
    };

    private final String[] heures = {
            "08:00 à 09:30",
            "09:30 à 11:00",
            "11:00 à 12:30",
            "12:30 à 14:00",
            "14:00 à 15:30",
            "15:30 à 17:00"
    };

    public void show(Stage stage, String login) {
    	
    	inscriptionController.archiverInscriptionsPassees();

   

        planningGrid.getStyleClass().add("planning-grid");
        planningGrid.setHgap(8);
        planningGrid.setVgap(8);

        chargerPlanning(login);

        ScrollPane scroll = new ScrollPane(planningGrid);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("activity-scroll");

        VBox content = new VBox(20, scroll);
        content.setPadding(new Insets(30));

        BorderPane root = new BorderPane();
        root.setTop(MemberNavbar.create(stage, "activites", login));
        root.setCenter(content);
        root.getStyleClass().add("page-background");

        Scene scene = new Scene(root, 1400, 800);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        stage.setTitle("Activités disponibles - OmniSport");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        
    }
    
    private String getJourAujourdhui() {

        DayOfWeek day = LocalDate.now().getDayOfWeek();

        switch (day) {
            case MONDAY:
                return "Lundi";
            case TUESDAY:
                return "Mardi";
            case WEDNESDAY:
                return "Mercredi";
            case THURSDAY:
                return "Jeudi";
            case FRIDAY:
                return "Vendredi";
            case SATURDAY:
                return "Samedi";
            case SUNDAY:
                return "Dimanche";
            default:
                return "";
        }
    }

    private boolean inscriptionAutorisee(Activite activite) {

        String jourActuel = getJourAujourdhui();

        if (!activite.getJour().equalsIgnoreCase(jourActuel)) {
            return false;
        }

        LocalTime maintenant = LocalTime.now();

        LocalTime debutAutorise = LocalTime.of(6, 0);

        String heureDebutText =
                activite.getHoraire().split(" à ")[0];

        LocalTime heureDebut =
                LocalTime.parse(heureDebutText);

        return !maintenant.isBefore(debutAutorise)
                && maintenant.isBefore(heureDebut);
           
      
    }

    private void chargerPlanning(String login) {

        planningGrid.getChildren().clear();
        planningGrid.getColumnConstraints().clear();

        ColumnConstraints heureCol = new ColumnConstraints();
        heureCol.setPrefWidth(78);
        heureCol.setMinWidth(78);
        heureCol.setMaxWidth(78);

        planningGrid.getColumnConstraints().add(heureCol);

        for (int i = 0; i < 6; i++) {
            ColumnConstraints dayCol = new ColumnConstraints();
            dayCol.setPrefWidth(170);
            dayCol.setMinWidth(170);
            planningGrid.getColumnConstraints().add(dayCol);
        }

        planningGrid.add(createHeaderCell(""), 0, 0);

        for (int j = 0; j < jours.length; j++) {
            planningGrid.add(createHeaderCell(jours[j]), j + 1, 0);
        }

        for (int i = 0; i < heures.length; i++) {

            planningGrid.add(createTimeCell(heures[i]), 0, i + 1);

            for (int j = 0; j < jours.length; j++) {

                List<Activite> activitesCellule =
                        getActivitesForCell(jours[j], heures[i]);

                planningGrid.add(
                        createPlanningCell(activitesCellule, login),
                        j + 1,
                        i + 1
                );
            }
        }
    }
    private List<Activite> getActivitesForCell(String jour, String horaire) {

        List<Activite> result = new ArrayList<>();

        for (Activite a : activiteController.getAllActivites()) {

            if (
                    a.getJour() != null
                    && a.getHoraire() != null
                    && a.getJour().equalsIgnoreCase(jour)
                    && a.getHoraire().equalsIgnoreCase(horaire)
            ) {
                result.add(a);
            }
        }

        return result;
    }

    private int findIndex(String[] array, String value) {

        if (value == null) {
            return -1;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i].equalsIgnoreCase(value.trim())) {
                return i;
            }
        }

        return -1;
    }

    private Label createHeaderCell(String text) {

        Label label = new Label(text);
        label.getStyleClass().add("planning-header-cell");
        label.setPrefSize(170, 55);

        return label;
    }
    private Label createTimeCell(String text) {

        Label label = new Label(text.replace(" à ", "\nà\n"));
        label.getStyleClass().add("planning-time-cell");
        label.setPrefSize(78, 80);

        return label;
    }

    private StackPane createPlanningCell(List<Activite> activites, String login) {

        StackPane cell = new StackPane();
        cell.getStyleClass().add("planning-cell");
        cell.setPrefSize(170, 80);

        VBox content = new VBox(4);
        content.setAlignment(Pos.CENTER);
        content.setFillWidth(true);

        if (activites != null && !activites.isEmpty()) {

            cell.getStyleClass().add("planning-cell-filled");

            for (Activite activite : activites) {

                Label name = new Label(activite.getNom());
                name.getStyleClass().add("planning-activity-name");

                StackPane activityLine = new StackPane(name);
                activityLine.getStyleClass().add("planning-activity-line");
                activityLine.setMaxWidth(Double.MAX_VALUE);

                activityLine.setOnMouseClicked(e -> {
                    e.consume();
                    ouvrirPopupInscription(activite, login);
                });

                content.getChildren().add(activityLine);
            }
        }

        cell.getChildren().add(content);

        return cell;
    }
    
    private Label createPopupInfo(String icon, String label, String value) {

        Label info = new Label(icon + "  " + label + " : " + value);
        info.getStyleClass().add("inscription-popup-info");
        info.setWrapText(true);

        return info;
    }
    private void ouvrirPopupInscription(Activite activite, String login) {

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Inscription");

        Label title = new Label(activite.getNom());
        title.getStyleClass().add("inscription-popup-title");

        Label description = createPopupInfo("📝", "Description", activite.getDescription());
        Label jour = createPopupInfo("📅", "Jour", activite.getJour());
        Label horaire = createPopupInfo("🕒", "Horaire", activite.getHoraire());
        Label capacite = createPopupInfo(
                "👥",
                "Capacité",
                activite.getCapaciteMax() + " places"
        );

        Label restrictionLabel = new Label();
        restrictionLabel.getStyleClass().add("inscription-popup-warning");
        restrictionLabel.setWrapText(true);
        restrictionLabel.setMaxWidth(360);

        Button inscrireBtn = new Button("S'inscrire");
        inscrireBtn.getStyleClass().add("inscription-popup-button");

        boolean autorisee = inscriptionAutorisee(activite);

        String statut = inscriptionController.getStatutInscription(
                login,
                activite.getId()
        );

        if (!autorisee) {

            inscrireBtn.setDisable(true);
            inscrireBtn.setText("Indisponible");

            restrictionLabel.setText(
                    "Inscription autorisée seulement\n"
                            + "le jour de la séance\n"
                            + "entre 06:00 et le début ❌"
            );

        } else {

            restrictionLabel.setText("Inscription disponible ✅");

            if (statut == null) {
                inscrireBtn.setDisable(false);
                inscrireBtn.setText("S'inscrire");
            } else if (statut.equals("EN_ATTENTE")) {
                inscrireBtn.setDisable(true);
                inscrireBtn.setText("En attente");
            } else if (statut.equals("ACCEPTEE")) {
                inscrireBtn.setDisable(true);
                inscrireBtn.setText("Déjà inscrit");
            } else if (statut.equals("REFUSEE")) {
                inscrireBtn.setDisable(false);
                inscrireBtn.setText("Redemander");
            }
        }

        inscrireBtn.setOnAction(e -> {

            boolean success = inscriptionController.inscrireMembre(
                    login,
                    activite.getId()
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inscription");
            alert.setHeaderText(null);

            if (success) {
                alert.setContentText("Demande d'inscription envoyée ✅");
                inscrireBtn.setDisable(true);
                inscrireBtn.setText("En attente");
            } else {
                alert.setContentText("Inscription impossible ❌ Vérifiez la console.");
            }

            alert.showAndWait();
        });

        VBox card = new VBox(
                18,
                title,
                description,
                jour,
                horaire,
                capacite,
                restrictionLabel,
                inscrireBtn
        );

        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("inscription-popup-card");

        StackPane root = new StackPane(card);
        root.setPadding(new Insets(28));
        root.getStyleClass().add("inscription-popup-root");

        Scene scene = new Scene(root, 520, 470);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        popup.setScene(scene);
        popup.showAndWait();
    }
}