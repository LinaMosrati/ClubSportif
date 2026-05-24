package view;

import javafx.geometry.Insets;
import projet.Main;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class Navbar {

    public static HBox create(Stage stage, String activePage) {

    	HBox topBar = new HBox(35);

    	topBar.setPrefHeight(85);

    	topBar.setMinHeight(85);

    	topBar.setMaxWidth(Double.MAX_VALUE);

    	topBar.prefWidthProperty().bind(
    	        stage.widthProperty()
    	);

    	topBar.setPadding(new Insets(20));

    	topBar.getStyleClass().add("topbar");

        Label logo = new Label("🏆 OmniSport");
        logo.getStyleClass().add("logo");

        Button accueilBtn = new Button("Accueil");
        Button membresBtn = new Button("Membres");
        Button activitesBtn = new Button("Activités");
        Button inscriptionsBtn = new Button("Inscriptions");
        Button financesBtn = new Button("Stat");
        Button logoutBtn = new Button("Déconnecter");

        styleButton(accueilBtn, activePage.equals("accueil"));
        styleButton(membresBtn, activePage.equals("membres"));
        styleButton(activitesBtn, activePage.equals("activites"));
        styleButton(financesBtn, activePage.equals("Stat"));
        styleButton(inscriptionsBtn, activePage.equals("inscriptions"));
        styleButton(logoutBtn, false);

        financesBtn.setOnAction(
        		e->
        		new StatView()
        		.show(stage)
        		);
        styleButton(
        		financesBtn,
        		activePage.equals("stat")
        		);
        accueilBtn.setOnAction(e -> new AdminDashboardView().show(stage));
        membresBtn.setOnAction(e -> new MembreView().show(stage));
        activitesBtn.setOnAction(e -> new ActiviteView().show(stage));
        inscriptionsBtn.setOnAction(e -> new InscriptionView().show(stage));
        logoutBtn.setOnAction(e -> {

            Main main = new Main();

            try {
                main.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        topBar.getChildren().addAll(
                logo,
                accueilBtn,
                membresBtn,
                activitesBtn,
                inscriptionsBtn,
                financesBtn,
                spacer,
                logoutBtn
        );

        return topBar;
        
        
        
    }

    private static void styleButton(Button button, boolean active) {

        if (active) {
            button.getStyleClass().add("nav-button-active");
        } else {
            button.getStyleClass().add("nav-button");
        }
    }
}