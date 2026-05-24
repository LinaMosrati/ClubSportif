package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import projet.Main;

public class MemberNavbar {

    public static HBox create(Stage stage, String activePage, String login) {

        HBox topBar = new HBox(35);
        topBar.setPadding(new Insets(20));
        topBar.getStyleClass().add("topbar");

        Label logo = new Label("🏆 OmniSport");
        logo.getStyleClass().add("logo");

        Button accueilBtn = new Button("Accueil");
        Button activitesBtn = new Button("Activités disponibles");
        Button inscriptionsBtn = new Button("Mes inscriptions");
        Button logoutBtn = new Button("Déconnecter");

        styleButton(accueilBtn, activePage.equals("accueil"));
        styleButton(activitesBtn, activePage.equals("activites"));
        styleButton(inscriptionsBtn, activePage.equals("inscriptions"));
        styleButton(logoutBtn, false);

        accueilBtn.setOnAction(e -> new MemberDashboardView().show(stage, login));
        activitesBtn.setOnAction(e -> {
            MemberActivitiesView view = new MemberActivitiesView();
            view.show(stage, login);
        });
        inscriptionsBtn.setOnAction(e -> {
            MemberInscriptionsView view = new MemberInscriptionsView();
            view.show(stage, login);
        });

        logoutBtn.setOnAction(e -> {
            try {
                new Main().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(
                logo,
                accueilBtn,
                activitesBtn,
                inscriptionsBtn,
                spacer,
                logoutBtn
        );

        return topBar;
    }

    private static void styleButton(Button button, boolean active) {
        button.getStyleClass().add(active ? "nav-button-active" : "nav-button");
    }
}