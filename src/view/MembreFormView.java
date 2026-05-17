package view;

import controller.MembreController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Membre;
import java.time.LocalDate;
import javafx.scene.control.DateCell;

public class MembreFormView {

    public void show(Stage stage) {

        Label title = new Label("Créer un compte membre");
        title.getStyleClass().add("page-title");

        TextField loginField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField nomField = new TextField();
        TextField prenomField = new TextField();
        DatePicker dateNaissancePicker = new DatePicker();
       

        dateNaissancePicker.setDayCellFactory(picker -> new DateCell() {

            @Override
            public void updateItem(LocalDate date, boolean empty) {

                super.updateItem(date, empty);

                if (
                        date.isAfter(LocalDate.now())
                        || date.isBefore(LocalDate.now().minusYears(100))
                ) {

                    setDisable(true);

                    setStyle(
                            "-fx-background-color: #ffc0cb;"
                    );
                }
            }
        });
        TextField adresseField = new TextField();
        TextField telephoneField = new TextField();
        TextField emailField = new TextField();
        TextField poidsField = new TextField();
        

        loginField.setPromptText("Login");
        passwordField.setPromptText("Mot de passe");
        nomField.setPromptText("Nom");
        prenomField.setPromptText("Prénom");
        adresseField.setPromptText("Adresse");
        telephoneField.setPromptText("Téléphone");
        emailField.setPromptText("Email");
        poidsField.setPromptText("Poids");
        
        Label dateError = new Label();     
        Label loginError = new Label();
        Label passwordError = new Label();
        Label telephoneError = new Label();
        Label emailError = new Label();
        Label poidsError = new Label();

        
        dateError.getStyleClass().add("field-error");
        loginError.getStyleClass().add("field-error");
        passwordError.getStyleClass().add("field-error");
        telephoneError.getStyleClass().add("field-error");
        emailError.getStyleClass().add("field-error");
        poidsError.getStyleClass().add("field-error");
        
        loginError.setTranslateY(-8);
        passwordError.setTranslateY(-8);
        telephoneError.setTranslateY(-8);
        emailError.setTranslateY(-8);
        poidsError.setTranslateY(-8);
        
        
        loginField.focusedProperty().addListener((obs, oldVal, newVal) -> {

            if (!newVal) {

                if (loginField.getText().trim().isEmpty()) {

                    loginError.setText("Login obligatoire");

                } else {

                    loginError.setText("");
                }
            }
        });

        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {

            if (!newVal) {

                if (passwordField.getText().length() < 4) {

                    passwordError.setText(
                            "Minimum 4 caractères"
                    );

                } else {

                    passwordError.setText("");
                }
            }
        });

        telephoneField.focusedProperty().addListener((obs, oldVal, newVal) -> {

            if (!newVal) {

            	if (!util.ValidationUtil.telephoneValide(telephoneField.getText())) {
            	    telephoneError.setText("8 chiffres, commence par 2,3,4,5,7 ou 9");
            	} else {
            	    telephoneError.setText("");
            	}
            }
        });

        emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {

            if (!newVal) {

            	if (!util.ValidationUtil.emailValide(emailField.getText())) {
            	    emailError.setText("Format invalide : exemple nom@gmail.com");
            	} else {
            	    emailError.setText("");
            	}
            }
        });

        poidsField.focusedProperty().addListener((obs, oldVal, newVal) -> {

            if (!newVal) {

                try {

                    double poids =
                            Double.parseDouble(
                                    poidsField.getText()
                            );

                    if (poids <= 0 || poids > 250) {

                        poidsError.setText(
                                "Poids invalide"
                        );

                    } else {

                        poidsError.setText("");
                    }

                } catch (Exception ex) {

                    poidsError.setText(
                            "Nombre invalide"
                    );
                }
            }
        });

        Button ajouterBtn = new Button("Ajouter membre");
        ajouterBtn.getStyleClass().add("login-button");

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("message");
        
        
       

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(4);
        form.setPadding(new Insets(25));
        form.getStyleClass().add("form-card");

        form.add(new Label("Login"), 0, 0);
        form.add(loginField, 1, 0);
        form.add(loginError, 1, 1);

        form.add(new Label("Mot de passe"), 0, 2);
        form.add(passwordField, 1, 2);
        form.add(passwordError, 1, 3);

        form.add(new Label("Nom"), 0, 4);
        form.add(nomField, 1, 4);

        form.add(new Label("Prénom"), 0, 5);
        form.add(prenomField, 1, 5);

        form.add(new Label("Date naissance"), 0, 6);
        form.add(dateNaissancePicker, 1, 6);
        form.add(dateError, 1, 7);

        form.add(new Label("Adresse"), 0, 7);
        form.add(adresseField, 1, 7);

        form.add(new Label("Téléphone"), 0, 8);
        form.add(telephoneField, 1, 8);
        form.add(telephoneError, 1, 9);

        form.add(new Label("Email"), 0, 10);
        form.add(emailField, 1, 10);
        form.add(emailError, 1, 11);

        form.add(new Label("Poids"), 0, 12);
        form.add(poidsField, 1, 12);
        form.add(poidsError, 1, 13);

        form.add(ajouterBtn, 1, 14);
        form.add(messageLabel, 1, 15);

        ajouterBtn.setOnAction(e -> {

            try {
                String dateNaissance = dateNaissancePicker.getValue().toString();
                double poids = Double.parseDouble(poidsField.getText());

                Membre membre = new Membre(
                        0,
                        loginField.getText(),
                        passwordField.getText(),
                        nomField.getText(),
                        prenomField.getText(),
                        dateNaissance,
                        adresseField.getText(),
                        telephoneField.getText(),
                        emailField.getText(),
                        poids
                );

                
                
                MembreController controller = new MembreController();

                String success = controller.ajouterMembre(
                        loginField.getText(),
                        passwordField.getText(),
                        nomField.getText(),
                        prenomField.getText(),
                        dateNaissance,
                        adresseField.getText(),
                        telephoneField.getText(),
                        emailField.getText(),
                        poidsField.getText()
                );
                messageLabel.setText(success);

                if (success.contains("succès")) {

                    loginField.clear();
                    passwordField.clear();
                    nomField.clear();
                    prenomField.clear();
                    adresseField.clear();
                    telephoneField.clear();
                    emailField.clear();
                    poidsField.clear();
                    dateNaissancePicker.setValue(null);
                }
            } catch (Exception ex) {
                messageLabel.setText("Vérifiez les champs saisis ❌");
                ex.printStackTrace();
            }
        });

        Button retourBtn = new Button("Retour dashboard");
        retourBtn.setOnAction(e -> {
            AdminDashboardView dashboard = new AdminDashboardView();
            dashboard.show(stage);
        });

        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(title, form, retourBtn);

        Scene scene = new Scene(root, 900, 700);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        stage.setTitle("Créer membre - OmniSport");
        stage.setScene(scene);
        stage.show();
    }
}