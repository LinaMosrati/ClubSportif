package view;

import controller.MembreController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Membre;

public class MembreView {

    private ListView<Membre> memberList = new ListView<>();
    private VBox detailBox = new VBox(18);
    private MembreController controller = new MembreController();
    private ObservableList<Membre> allMembers = FXCollections.observableArrayList();
    private Pagination pagination = new Pagination();
    private static final int ITEMS_PER_PAGE = 10;

    public void show(Stage stage) {

        Label title = new Label("Gestion des membres");
        title.getStyleClass().add("page-title");

        Button ajouterBtn = new Button("Créer un membre");
        TextField searchField = new TextField();

        searchField.setPromptText(
                "🔍 Rechercher par nom ou téléphone..."
        );

        searchField.getStyleClass().add(
                "member-search"
        );

        searchField.textProperty().addListener(
                (obs, oldVal, newVal) -> {

                    filterMembers(newVal);

                }
        );

      
        HBox topActions =
                new HBox(
                        18,
                        ajouterBtn,
                        searchField
                        
                );

        topActions.setAlignment(
                Pos.CENTER_LEFT
        );
        ajouterBtn.getStyleClass().add("login-button");
        ajouterBtn.setOnAction(e -> ouvrirPopupAjout());

        chargerMembres();

        memberList.getStyleClass().add("member-list");
        memberList.setPrefWidth(340);
        memberList.setMaxWidth(340);

        memberList.setPrefHeight(430);
        memberList.setMaxHeight(430);

        detailBox.setPrefHeight(430);
        detailBox.setMaxHeight(430);
        memberList.setCellFactory(list -> new ListCell<>() {

            @Override
            protected void updateItem(Membre membre, boolean empty) {
                super.updateItem(membre, empty);

                if (empty || membre == null) {
                    setGraphic(null);
                } else {
                    VBox card = new VBox(6);

                    Label name = new Label(membre.getNom() + " " + membre.getPrenom());
                    name.getStyleClass().add("member-card-name");

                  

                    card.getChildren().addAll(name);
                    card.getStyleClass().add("member-small-card");

                    setText(null);
                    setGraphic(card);
                }
            }
        });

        memberList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, selected) -> {
                    afficherDetails(selected);
                }
        );

        detailBox.getStyleClass().add("member-detail-card");
        detailBox.setPadding(new Insets(30));

        Label emptyLabel = new Label("Sélectionnez un membre pour voir ses informations");
        emptyLabel.getStyleClass().add("member-empty-text");
        detailBox.getChildren().add(emptyLabel);

        VBox leftPanel = new VBox(12, memberList, pagination);
        leftPanel.setPrefWidth(360);
        leftPanel.setMaxWidth(360);

        ScrollPane detailScroll = new ScrollPane(detailBox);
        detailScroll.setFitToWidth(true);
        detailScroll.setPrefHeight(430);
        detailScroll.setMaxHeight(430);
        detailScroll.getStyleClass().add("member-detail-scroll");

        HBox mainContent = new HBox(25, leftPanel, detailScroll);
        HBox.setHgrow(detailScroll, Priority.ALWAYS);
        HBox.setHgrow(detailBox, Priority.ALWAYS);
        VBox content =
        		new VBox(
        		22,
        		title,
        		topActions,
        		mainContent
        		);
        content.setPadding(new Insets(30));

        BorderPane root = new BorderPane();
        root.setTop(Navbar.create(stage, "membres"));
        root.setCenter(content);
        root.getStyleClass().add("page-background");

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        stage.setTitle("Gestion des membres - OmniSport");
        stage.setScene(scene);
        stage.show();
    }
    
    private void filterMembers(String keyword) {

        if (
                keyword == null ||
                keyword.isBlank()
        ) {

            updateMemberPage(
                    pagination
                    .getCurrentPageIndex()
            );

            return;
        }

        ObservableList<Membre> filtered =
                allMembers.filtered(
                        m ->

                        (
                        m.getNom()
                        .toLowerCase()
                        .contains(
                                keyword
                                .toLowerCase()
                        )

                        ||

                        m.getPrenom()
                        .toLowerCase()
                        .contains(
                                keyword
                                .toLowerCase()
                        )

                        ||

                        m.getTelephone()
                        .contains(
                                keyword
                        )
                )

        );

        memberList.setItems(
                filtered
        );

        pagination.setVisible(
                false
        );
    }

    private void chargerMembres() {
        allMembers.setAll(controller.getAllMembres());

        int pageCount = (int) Math.ceil((double) allMembers.size() / ITEMS_PER_PAGE);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setCurrentPageIndex(0);

        updateMemberPage(0);

        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            updateMemberPage(newIndex.intValue());
        });
        pagination.getStyleClass().add("member-pagination");
        pagination.setMaxPageIndicatorCount(5);
    }
    
  
    private void updateMemberPage(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, allMembers.size());

        if (fromIndex > toIndex) return;

        ObservableList<Membre> pageMembers = FXCollections.observableArrayList(
                allMembers.subList(fromIndex, toIndex)
        );

        memberList.setItems(pageMembers);
    }

    private void afficherDetails(Membre membre) {

        detailBox.getChildren().clear();

        if (membre == null) {
            Label emptyLabel = new Label("Sélectionnez un membre pour voir ses informations");
            emptyLabel.getStyleClass().add("member-empty-text");
            detailBox.getChildren().add(emptyLabel);
            return;
        }

        Label avatar = new Label(
                membre.getNom().substring(0, 1).toUpperCase()
                        + membre.getPrenom().substring(0, 1).toUpperCase()
        );
        avatar.getStyleClass().add("member-avatar");

        Label fullName = new Label(membre.getNom() + " " + membre.getPrenom());
        fullName.getStyleClass().add("member-detail-name");

        HBox login = createInfo("Login", membre.getLogin());
        HBox naissance = createInfo("Date naissance", membre.getDateNaissance());

        HBox adresse = createInfo("Adresse", membre.getAdresse());
        HBox telephone = createInfo("Téléphone", membre.getTelephone());

        HBox email = createInfo("Email", membre.getEmail());
        HBox poids = createInfo("Poids", membre.getPoids() + " kg");
        Button modifierBtn = new Button("Modifier");
        modifierBtn.getStyleClass().add("edit-button");

        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.getStyleClass().add("delete-button");

        modifierBtn.setOnAction(e -> ouvrirPopupModification(membre));

        supprimerBtn.setOnAction(e -> afficherConfirmationSuppression(membre));
        HBox header = new HBox(18, avatar, fullName);
        header.setAlignment(Pos.CENTER_LEFT);

        GridPane infos = new GridPane();
        infos.setHgap(35);
        infos.setVgap(14);

        infos.add(createInfo("Login", membre.getLogin()), 0, 0);
        infos.add(createInfo("Date naissance", membre.getDateNaissance()), 1, 0);

        infos.add(createInfo("Adresse", membre.getAdresse()), 0, 1);
        infos.add(createInfo("Téléphone", membre.getTelephone()), 1, 1);

        infos.add(createInfo("Email", membre.getEmail()), 0, 2);
        infos.add(createInfo("Poids", membre.getPoids() + " kg"), 1, 2);

        HBox actions = new HBox(12, modifierBtn, supprimerBtn);
        actions.setAlignment(Pos.CENTER_LEFT);

        detailBox.getChildren().addAll(
                header,
                infos,
                actions
        );
        
    }

    private HBox createInfo(String label, String value) {

        Label title = new Label(label + " : ");
        title.getStyleClass().add("member-info-title");

        Label content = new Label(value);
        content.getStyleClass().add("member-info-value");

        HBox box = new HBox(4, title, content);

        return box;
    }

    private void afficherConfirmationSuppression(Membre membre) {

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);

        Label icon = new Label("⚠");
        icon.getStyleClass().add("warning-icon");

        Label title = new Label("Supprimer ce membre ?");
        title.getStyleClass().add("popup-title");

        Label text = new Label("Voulez-vous vraiment supprimer " + membre.getNom() + " ?");
        text.getStyleClass().add("popup-message");

        Button annulerBtn = new Button("Annuler");
        annulerBtn.getStyleClass().add("cancel-button");

        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.getStyleClass().add("delete-buttonbox");

        annulerBtn.setOnAction(e -> popup.close());

        supprimerBtn.setOnAction(e -> {

            boolean success = controller.supprimerMembre(
                    membre.getId(),
                    membre.getLogin()
            );

            if (success) {
                popup.close();
                chargerMembres();
                detailBox.getChildren().clear();

                Label emptyLabel = new Label("Sélectionnez un membre pour voir ses informations");
                emptyLabel.getStyleClass().add("member-empty-text");
                detailBox.getChildren().add(emptyLabel);
            }
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

    private void ouvrirPopupModification(Membre membre) {

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Modifier membre");

        Label title = new Label("Modifier le membre");
        title.getStyleClass().add("page-title");

        TextField nomField = new TextField(membre.getNom());
        TextField prenomField = new TextField(membre.getPrenom());
        DatePicker datePicker = new DatePicker(java.time.LocalDate.parse(membre.getDateNaissance()));
        TextField adresseField = new TextField(membre.getAdresse());
        TextField telephoneField = new TextField(membre.getTelephone());
        TextField emailField = new TextField(membre.getEmail());
        TextField poidsField = new TextField(String.valueOf(membre.getPoids()));

        Button modifierBtn = new Button("Modifier");
        modifierBtn.getStyleClass().add("login-button");

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("message");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(6);
        form.setPadding(new Insets(35));
        form.setMaxWidth(620);
        form.getStyleClass().add("form-card");

        form.add(new Label("Nom"), 0, 0);
        form.add(nomField, 1, 0);

        form.add(new Label("Prénom"), 0, 1);
        form.add(prenomField, 1, 1);

        form.add(new Label("Date naissance"), 0, 2);
        form.add(datePicker, 1, 2);

        form.add(new Label("Adresse"), 0, 3);
        form.add(adresseField, 1, 3);

        form.add(new Label("Téléphone"), 0, 4);
        form.add(telephoneField, 1, 4);

        form.add(new Label("Email"), 0, 5);
        form.add(emailField, 1, 5);

        form.add(new Label("Poids"), 0, 6);
        form.add(poidsField, 1, 6);

        form.add(modifierBtn, 1, 7);
        form.add(messageLabel, 1, 8);

        modifierBtn.setOnAction(e -> {
            try {
                Membre membreModifie = new Membre(
                        membre.getId(),
                        membre.getLogin(),
                        membre.getMotDePasse(),
                        nomField.getText(),
                        prenomField.getText(),
                        datePicker.getValue().toString(),
                        adresseField.getText(),
                        telephoneField.getText(),
                        emailField.getText(),
                        Double.parseDouble(poidsField.getText())
                );

                String resultat = controller.modifierMembre(membreModifie);
                messageLabel.setText(resultat);

                if (resultat.contains("succès")) {
                    popup.close();
                    chargerMembres();
                    afficherDetails(membreModifie);
                }

            } catch (Exception ex) {
                messageLabel.setText("Vérifiez les champs ❌");
            }
        });

        VBox root = new VBox(20, title, form);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f5f7fb;");

        Scene scene = new Scene(root, 600, 620);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        popup.setScene(scene);
        popup.showAndWait();
    }

    private void ouvrirPopupAjout() {

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Ajouter membre");

        MembreFormView formView = new MembreFormView();

        VBox root = formView.createFormContent(() -> {
            popup.close();
            chargerMembres();
        });

        Scene scene = new Scene(root, 650, 720);
        scene.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
        );

        popup.setScene(scene);
        popup.showAndWait();
    }
}