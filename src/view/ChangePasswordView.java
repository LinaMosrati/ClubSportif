package view;

import controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.MemberDashboardView;

public class ChangePasswordView {

    public void show(Stage stage, String login) {

        Label title = new Label("Changer votre mot de passe");
        title.getStyleClass().add("page-title");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Nouveau mot de passe");
        newPasswordField.getStyleClass().add("input");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirmer le mot de passe");
        confirmPasswordField.getStyleClass().add("input");

        Button saveBtn = new Button("Enregistrer");
        saveBtn.getStyleClass().add("login-button");

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("message");

        saveBtn.setOnAction(e -> {

            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (newPassword.length() < 4) {
                messageLabel.setText("Mot de passe minimum 4 caractères ❌");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                messageLabel.setText("Les mots de passe ne correspondent pas ❌");
                return;
            }

            LoginController controller =
                    new LoginController();

            String resultat =
                    controller.changerMotDePasse(
                            login,
                            newPassword,
                            confirmPassword
                    );
            messageLabel.setText(resultat);

            if (resultat.contains("succès")) {
                MemberDashboardView memberView = new MemberDashboardView();
                memberView.show(stage, login);
            }
            if (resultat.contains("succès")) {

                newPasswordField.clear();
                confirmPasswordField.clear();
            }
        });

        VBox root = new VBox(18);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(
                title,
                newPasswordField,
                confirmPasswordField,
                saveBtn,
                messageLabel
        );

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        stage.setTitle("Première connexion - OmniSport");
        stage.setScene(scene);
        stage.show();
    }
}