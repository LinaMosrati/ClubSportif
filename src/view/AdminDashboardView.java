package view;

import view.MembreFormView;
import javafx.scene.control.ScrollPane;
import controller.AdminController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AdminDashboardView {

    public void show(Stage stage) {

    	HBox topBar = Navbar.create(stage, "accueil");
    	
    	AdminController controller = new AdminController();

    	int nbMembres = controller.getNombreMembres();
    	int nbActivites = controller.getNombreActivites();
    	int tauxRemplissage = controller.getTauxRemplissage();
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

        VBox content = new VBox(22);

        content.setPadding(
                new Insets(
                        20,
                        40,
                        30,
                        40
                )
        );

        content.getStyleClass()
        .add("dashboard-content");

        VBox hero = new VBox(8);

        hero.getStyleClass()
        .add("hero-panel");

        Label welcome =
        new Label(
        "Bonjour Administrateur 👋"
        );

        welcome.getStyleClass()
        .add("hero-title");

        Label sub =
        new Label(
        "Salle active • Gestion intelligente"
        );

        sub.getStyleClass()
        .add("hero-sub");

        hero.getChildren()
        .addAll(
        welcome,
        sub
        );
        welcome.getStyleClass().add("welcome-title");

        GridPane stats =
        		new GridPane();

        		stats.setHgap(22);

        		stats.add(
        		createCard(
        		String.valueOf(nbMembres),
        		"👥 Membres"
        		),
        		0,
        		0
        		);

        		stats.add(
        		createCard(
        		String.valueOf(nbActivites),
        		"🏃 Activités"
        		),
        		1,
        		0
        		);

        		stats.add(
        		createCard(
        		tauxRemplissage+"%",
        		"📈 Remplissage"
        		),
        		2,
        		0
        		);

        		stats.add(
        		createCard(
        		"12",
        		"📦 Archives"
        		),
        		3,
        		0
        		);
        VBox card1 = createCard(String.valueOf(nbMembres), "Membres");
        VBox card2 = createCard(String.valueOf(nbActivites), "Activités");
        VBox card3 = createCard(tauxRemplissage + "%", "Remplissage");

        stats.getChildren().addAll(card1, card2, card3);

        java.util.List<model.Activite> activites =
                controller.getDashboardActivites();

        FlowPane activities = new FlowPane();
        activities.setHgap(20);
        activities.setVgap(20);
        activities.setPrefWrapLength(1000);
        
        for (model.Activite activite : activites) {

            VBox card = createActivityCard(
                    activite.getNom(),
                    activite.getParticipants() + " / " + activite.getCapaciteMax()
            );

            activities.getChildren().add(card);
        }

        Label section =
        		new Label(
        		"Activités populaires"
        		);

        		section.getStyleClass()
        		.add("section-title");

        		content.getChildren()
        		.addAll(
        		hero,
        		stats,
        		section,
        		activities
        		);
        // =========================
        // ROOT
        // =========================

        BorderPane root = new BorderPane();

        root.setTop(topBar);

        root.setLeft(sideBar);

        ScrollPane scroll = new ScrollPane();

        scroll.setContent(content);

        scroll.setFitToWidth(true);

        scroll.setFitToHeight(false);

        scroll.setPannable(true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scroll.getStyleClass()
        .add("dashboard-scroll");

        root.setCenter(scroll);

        Scene scene = new Scene(root, 1400, 800);

        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        stage.setTitle("OmniSport Dashboard");

        stage.setScene(scene);
        stage.setMaximized(true);

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