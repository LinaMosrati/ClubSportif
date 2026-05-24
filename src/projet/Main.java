package projet;


import view.ChangePasswordView;

import view.MemberDashboardView;
import dao.UserDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.User;
import view.AdminDashboardView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Label logo = new Label("🏆 OmniSport");
        logo.getStyleClass().add("title");

        Label subtitle = new Label("Connectez-vous à votre espace");
        subtitle.getStyleClass().add("subtitle");

        TextField loginField = new TextField();
        loginField.setPromptText("Login");
        loginField.getStyleClass().add("input");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.getStyleClass().add("input");

        Button loginButton = new Button("Se connecter");
        loginButton.getStyleClass().add("login-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("message");

        
        loginButton.setOnAction(e -> {

            String login = loginField.getText();
            String password = passwordField.getText();

            UserDAO userDAO = new UserDAO();

            User user =
                    userDAO.verifierConnexion(
                            login,
                            password
                    );

            if (user == null) {
                messageLabel.setText("Login ou mot de passe incorrect");
            } 
            else if (user.getRole().equals("ADMIN")) {
                AdminDashboardView adminDashboard = new AdminDashboardView();
                adminDashboard.show(stage);
            } 
            else if (
                    user.getRole().equals("MEMBRE")
            ) {

                if (user.isPremiereConnexion()) {

                    ChangePasswordView view =
                            new ChangePasswordView();

                    view.show(stage, login);

                } else {

                	MemberDashboardView memberView = new MemberDashboardView();
                	memberView.show(stage, login);
                }
            }
        });

        VBox card = new VBox(18);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(380);
        card.getStyleClass().add("login-card");

        card.getChildren().addAll(
                logo,
                subtitle,
                loginField,
                passwordField,
                loginButton,
                messageLabel
        );

        StackPane root = new StackPane(card);
        root.setPadding(new Insets(40));

        Scene scene = new Scene(root, 900, 550);

        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        stage.setTitle("Connexion - OmniSport");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}