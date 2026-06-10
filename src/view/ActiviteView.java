package view;

import controller.ActiviteController;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.DayOfWeek;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Activite;

public class ActiviteView {

    private GridPane planningGrid = new GridPane();
    private ActiviteController controller = new ActiviteController();

    private final String[] jours = {
            "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"
    };

    private final String[] heures = {
            "08:00 à 09:30",
            "09:30 à 11:00",
            "11:00 à 12:30",
            "12:30 à 14:00",
            "14:00 à 15:30",
            "15:30 à 17:00",
            "17:30 à 19:00"
    };

    public void show(Stage stage) {

      

        Button ajouterBtn = new Button("Créer une activité");
        ajouterBtn.getStyleClass().add("login-button");
        ajouterBtn.setOnAction(e -> ouvrirPopupAjout());

        planningGrid.getStyleClass().add("planning-grid");
        planningGrid.setHgap(8);
        planningGrid.setVgap(8);
        chargerActivites();

        ScrollPane scroll = new ScrollPane(planningGrid);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("activity-scroll");

        VBox content = new VBox(20, ajouterBtn, scroll);
        content.setPadding(new Insets(30));

        BorderPane root = new BorderPane();
        root.setTop(Navbar.create(stage, "activites"));
        root.setCenter(content);
        root.getStyleClass().add("page-background");

        Scene scene = new Scene(root, 1400, 800);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void chargerActivites() {
    	
    	

    	planningGrid.getChildren().clear();
    	planningGrid.getColumnConstraints().clear();

    	/* heure */
    	ColumnConstraints heureCol =
    	new ColumnConstraints();

    	heureCol.setPrefWidth(78);
    	heureCol.setMinWidth(78);

    	/* jours */
    	for(int i=0;i<6;i++){

    	    ColumnConstraints dayCol =
    	            new ColumnConstraints();

    	    dayCol.setPrefWidth(170);

    	    planningGrid
    	            .getColumnConstraints()
    	            .add(dayCol);
    	}

    	/* add heure first */
    	planningGrid
    	        .getColumnConstraints()
    	        .add(
    	                0,
    	                heureCol
    	        );

    	planningGrid.add(
    	        createHeaderCell(""),
    	        0,
    	        0
    	);

        for (int j = 0; j < jours.length; j++) {
            planningGrid.add(createHeaderCell(jours[j]), j + 1, 0);
        }

        for (int i = 0; i < heures.length; i++) {

            planningGrid.add(createTimeCell(heures[i]), 0, i + 1);

            for (int j = 0; j < jours.length; j++) {

                List<Activite> activitesCellule =
                        getActivitesForCell(jours[j], heures[i]);

                planningGrid.add(
                        createPlanningCell(activitesCellule),
                        j + 1,
                        i + 1
                );
            }
        }
    }
    
    private List<Activite> getActivitesForCell(String jour, String horaire) {

        List<Activite> result = new ArrayList<>();

        for (Activite a : controller.getAllActivites()) {

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
    
    private boolean estLeJourDeActivite(String jourActivite) {

        DayOfWeek today = LocalDate.now().getDayOfWeek();

        String jourAujourdhui = "";

        switch (today) {
            case MONDAY:
                jourAujourdhui = "Lundi";
                break;

            case TUESDAY:
                jourAujourdhui = "Mardi";
                break;

            case WEDNESDAY:
                jourAujourdhui = "Mercredi";
                break;

            case THURSDAY:
                jourAujourdhui = "Jeudi";
                break;

            case FRIDAY:
                jourAujourdhui = "Vendredi";
                break;

            case SATURDAY:
                jourAujourdhui = "Samedi";
                break;

            case SUNDAY:
                jourAujourdhui = "Dimanche";
                break;
        }

        return jourAujourdhui.equalsIgnoreCase(jourActivite);
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
        label.setMaxWidth(Double.MAX_VALUE);

        return label;
    }

    private Label createTimeCell(String text) {

    	Label label = new Label(text.replace(" à ", "\nà\n"));
        label.getStyleClass().add("planning-time-cell");
        label.setMaxWidth(Double.MAX_VALUE);

        return label;
    }

    private StackPane createPlanningCell(List<Activite> activites) {

        StackPane cell = new StackPane();
        cell.getStyleClass().add("planning-cell");

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
                    ouvrirPopupModification(activite);
                });

                content.getChildren().add(activityLine);
            }
        }

        cell.getChildren().add(content);

        return cell;
    }

    private ComboBox<String> createJourBox() {

        ComboBox<String> jourBox = new ComboBox<>();
        jourBox.getItems().addAll(jours);
        jourBox.setPromptText("Choisir le jour");
        jourBox.setMaxWidth(Double.MAX_VALUE);

        return jourBox;
    }

    private ComboBox<String> createHoraireBox() {

        ComboBox<String> horaireBox = new ComboBox<>();
        horaireBox.getItems().addAll(heures);
        horaireBox.setPromptText("Choisir l'horaire");
        horaireBox.setMaxWidth(Double.MAX_VALUE);

        return horaireBox;
    }

    private void ouvrirPopupAjout() {

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Ajouter activité");

        Label title = new Label("Créer une activité");
        title.getStyleClass().add("page-title");

        TextField nomField = new TextField();
        TextArea descriptionField = new TextArea();
        TextField capaciteField = new TextField();

        ComboBox<String> jourBox = createJourBox();
        ComboBox<String> horaireBox = createHoraireBox();

        nomField.setPromptText("Nom de l'activité");
        descriptionField.setPromptText("Description");
        capaciteField.setPromptText("Capacité max");
        descriptionField.setWrapText(true);
        descriptionField.setPrefRowCount(3);

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.getStyleClass().add("login-button");

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("message");

        GridPane form = new GridPane();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(120);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setMinWidth(480);

        form.getColumnConstraints().addAll(col1, col2);

        form.setHgap(12);
        form.setVgap(10);
        form.setPadding(new Insets(35));
        form.setMaxWidth(650);
        form.getStyleClass().add("form-card");

        form.add(new Label("Nom"), 0, 0);
        form.add(nomField, 1, 0);

        form.add(new Label("Description"), 0, 1);
        form.add(descriptionField, 1, 1);

        form.add(new Label("Capacité max"), 0, 2);
        form.add(capaciteField, 1, 2);

        form.add(new Label("Jour"), 0, 3);
        form.add(jourBox, 1, 3);

        form.add(new Label("Horaire"), 0, 4);
        form.add(horaireBox, 1, 4);

        form.add(ajouterBtn, 1, 5);
        form.add(messageLabel, 1, 6);

        ajouterBtn.setOnAction(e -> {

            if (jourBox.getValue() == null || horaireBox.getValue() == null) {
                messageLabel.setText("Choisissez le jour et l'horaire ❌");
                return;
            }

            String resultat = controller.ajouterActivite(
                    nomField.getText(),
                    descriptionField.getText(),
                    capaciteField.getText(),
                    jourBox.getValue(),
                    horaireBox.getValue()
            );

            messageLabel.setText(resultat);

            if (resultat.contains("succès")) {
                popup.close();
                chargerActivites();
            }
        });

        VBox root = new VBox(20, title, form);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(35));
        root.getStyleClass().add("page-background");

        Scene scene = new Scene(root, 700, 600);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        popup.setScene(scene);
        popup.showAndWait();
    }

    private void ouvrirPopupModification(Activite activite) {

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Modifier activité");

        Label title = new Label("Modifier l'activité");
        title.getStyleClass().add("page-title");

        TextField nomField = new TextField(activite.getNom());
        TextArea descriptionField = new TextArea(activite.getDescription());
        TextField capaciteField = new TextField(String.valueOf(activite.getCapaciteMax()));

        ComboBox<String> jourBox = createJourBox();
        jourBox.setValue(activite.getJour());

        ComboBox<String> horaireBox = createHoraireBox();
        horaireBox.setValue(activite.getHoraire());

        descriptionField.setWrapText(true);
        descriptionField.setPrefRowCount(3);

        Button modifierBtn = new Button("Modifier");
        modifierBtn.getStyleClass().add("login-button");
        
        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.getStyleClass().add("delete-button");

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("message");

        GridPane form = new GridPane();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(120);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setMinWidth(480);

        form.getColumnConstraints().addAll(col1, col2);

        form.setHgap(12);
        form.setVgap(10);
        form.setPadding(new Insets(35));
        form.setMaxWidth(650);
        form.getStyleClass().add("form-card");

        form.add(new Label("Nom"), 0, 0);
        form.add(nomField, 1, 0);

        form.add(new Label("Description"), 0, 1);
        form.add(descriptionField, 1, 1);

        form.add(new Label("Capacité max"), 0, 2);
        form.add(capaciteField, 1, 2);

        form.add(new Label("Jour"), 0, 3);
        form.add(jourBox, 1, 3);

        form.add(new Label("Horaire"), 0, 4);
        form.add(horaireBox, 1, 4);

        HBox actions = new HBox(12, modifierBtn, supprimerBtn);
        actions.setAlignment(Pos.CENTER_LEFT);

        form.add(actions, 1, 5);
        form.add(messageLabel, 1, 6);
        
        supprimerBtn.setOnAction(e -> {

            if (estLeJourDeActivite(activite.getJour())) {
                messageLabel.setText("Impossible de supprimer cette activité aujourd'hui ❌");
                return;
            }

            boolean success = controller.supprimerActivite(activite.getId());

            if (success) {
                popup.close();
                chargerActivites();
            } else {
                messageLabel.setText("Erreur lors de la suppression ❌");
            }
        });

        modifierBtn.setOnAction(e -> {
            try {

                if (jourBox.getValue() == null || horaireBox.getValue() == null) {
                    messageLabel.setText("Choisissez le jour et l'horaire ❌");
                    return;
                }

                Activite activiteModifiee = new Activite(
                        activite.getId(),
                        nomField.getText(),
                        descriptionField.getText(),
                        Integer.parseInt(capaciteField.getText()),
                        jourBox.getValue(),
                        horaireBox.getValue(),
                        0
                );

                String resultat = controller.modifierActivite(activiteModifiee);
                messageLabel.setText(resultat);

                if (resultat.contains("succès")) {
                    popup.close();
                    chargerActivites();
                }

            } catch (Exception ex) {
                messageLabel.setText("Vérifiez les champs ❌");
            }
        });

        VBox root = new VBox(20, title, form);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(35));
        root.getStyleClass().add("page-background");

        Scene scene = new Scene(root, 700, 600);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        popup.setScene(scene);
        popup.showAndWait();
    }

    private void afficherConfirmationSuppression(Activite activite) {

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initStyle(StageStyle.UNDECORATED);

        Label icon = new Label("⚠");
        icon.getStyleClass().add("warning-icon");

        Label title = new Label("Supprimer cette activité ?");
        title.getStyleClass().add("popup-title");

        Label text = new Label("Voulez-vous vraiment supprimer " + activite.getNom() + " ?");
        text.getStyleClass().add("popup-message");

        Button annulerBtn = new Button("Annuler");
        annulerBtn.getStyleClass().add("cancel-button");

        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.getStyleClass().add("delete-button");

        annulerBtn.setOnAction(e -> popup.close());

        supprimerBtn.setOnAction(e -> {
            boolean success = controller.supprimerActivite(activite.getId());

            if (success) {
                chargerActivites();
            }

            popup.close();
        });

        HBox buttons = new HBox(15, annulerBtn, supprimerBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(18, icon, title, text, buttons);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("confirm-box");

        Scene scene = new Scene(root, 420, 260);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        popup.setScene(scene);
        popup.showAndWait();
    }
}