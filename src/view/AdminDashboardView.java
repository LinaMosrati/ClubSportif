package view;

import view.MembreFormView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AdminDashboardView {

    public void show(Stage stage) {

        // =========================
        // TOP BAR
        // =========================

        HBox topBar = new HBox(30);

        topBar.setPadding(new Insets(20));

        topBar.getStyleClass().add("topbar");

        Label logo = new Label("🏆 OmniSport");

        logo.getStyleClass().add("logo");

        Button accueilBtn = new Button("Accueil");

        Button membresBtn = new Button("Membres");
        
        membresBtn.setOnAction(e -> {
            MembreFormView membreFormView = new MembreFormView();
            membreFormView.show(stage);
        });

        Button activitesBtn = new Button("Activités");

        Button financesBtn = new Button("Finances");

        accueilBtn.getStyleClass().add("nav-button");
        membresBtn.getStyleClass().add("nav-button");
        activitesBtn.getStyleClass().add("nav-button");
        financesBtn.getStyleClass().add("nav-button");

        topBar.getChildren().addAll(
                logo,
                accueilBtn,
                membresBtn,
                activitesBtn,
                financesBtn
        );

        // =========================
        // SIDEBAR
        // =========================

        VBox sideBar = new VBox(25);

        sideBar.setPadding(new Insets(20));

        sideBar.getStyleClass().add("sidebar");

        Button dashboardBtn = new Button("🏠");

        Button usersBtn = new Button("👥");
       
        
        usersBtn.setOnAction(e -> {
            MembreFormView membreFormView = new MembreFormView();
            membreFormView.show(stage);
        });

        Button classesBtn = new Button("🏃");

        Button reportsBtn = new Button("📋");
        
        Button retourBtn = new Button("Retour dashboard");

        retourBtn.setOnAction(e -> {
            AdminDashboardView dashboard = new AdminDashboardView();
            dashboard.show(stage);
        });

        dashboardBtn.getStyleClass().add("sidebar-button");
        usersBtn.getStyleClass().add("sidebar-button");
        classesBtn.getStyleClass().add("sidebar-button");
        reportsBtn.getStyleClass().add("sidebar-button");

        sideBar.getChildren().addAll(
                dashboardBtn,
                usersBtn,
                classesBtn,
                reportsBtn
        );

        // =========================
        // CONTENT
        // =========================

        VBox content = new VBox(25);

        content.setPadding(new Insets(30));

        Label welcome = new Label("Bonjour Administrateur 👋");

        welcome.getStyleClass().add("welcome-title");

        HBox stats = new HBox(20);

        VBox card1 = createCard("248", "Membres");

        VBox card2 = createCard("14", "Activités");

        VBox card3 = createCard("92%", "Remplissage");

        stats.getChildren().addAll(card1, card2, card3);

        HBox activities = new HBox(20);

        VBox natation = createActivityCard(
                "Natation",
                "18 / 20"
        );

        VBox yoga = createActivityCard(
                "Yoga",
                "11 / 15"
        );

        VBox muscu = createActivityCard(
                "Musculation",
                "25 / 25"
        );

        activities.getChildren().addAll(
                natation,
                yoga,
                muscu
        );

        content.getChildren().addAll(
                welcome,
                stats,
                activities
        );

        // =========================
        // ROOT
        // =========================

        BorderPane root = new BorderPane();

        root.setTop(topBar);

        root.setLeft(sideBar);

        root.setCenter(content);

        Scene scene = new Scene(root, 1400, 800);

        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        stage.setTitle("OmniSport Dashboard");

        stage.setScene(scene);

        stage.show();
    }

    // =========================
    // CARD
    // =========================

    private VBox createCard(
            String number,
            String text
    ) {

        VBox card = new VBox(10);

        card.setAlignment(Pos.CENTER);

        card.setPadding(new Insets(25));

        card.setPrefWidth(180);

        card.getStyleClass().add("stat-card");

        Label numberLabel = new Label(number);

        numberLabel.getStyleClass().add("card-number");

        Label textLabel = new Label(text);

        textLabel.getStyleClass().add("card-text");

        card.getChildren().addAll(
                numberLabel,
                textLabel
        );

        return card;
    }

    // =========================
    // ACTIVITY CARD
    // =========================

    private VBox createActivityCard(
            String title,
            String places
    ) {

        VBox card = new VBox(15);

        card.setPadding(new Insets(25));

        card.setPrefWidth(320);

        card.getStyleClass().add("activity-card");

        Label titleLabel = new Label(title);

        titleLabel.getStyleClass().add("activity-title");

        Label placesLabel = new Label(places);

        placesLabel.getStyleClass().add("activity-places");

        Region progress = new Region();

        progress.setPrefHeight(10);

        progress.getStyleClass().add("progress-bar");

        card.getChildren().addAll(
                titleLabel,
                placesLabel,
                progress
        );

        return card;
    }
}