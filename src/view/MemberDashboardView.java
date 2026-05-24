package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MemberDashboardView {

	public void show(Stage stage, String login) {

		Label welcome =
		new Label(
		"Bonjour " + login + " 👋"
		);

		welcome.getStyleClass()
		.add("hero-title");

		Label sub =
		new Label(
		"Prêt pour votre prochaine séance ?"
		);

		sub.getStyleClass()
		.add("hero-sub");

		VBox hero =
		new VBox(
		10,
		welcome,
		sub
		);

		hero.getStyleClass()
		.add("member-hero");







		VBox nextActivity =
		new VBox(12);

		nextActivity
		.getStyleClass()
		.add("next-card");

		Label nextTitle =
		new Label(
		"Prochaine séance"
		);

		nextTitle
		.getStyleClass()
		.add("section-title");

		Label nextSport =
		new Label(
		"Pilates"
		);

		nextSport
		.getStyleClass()
		.add("next-sport");

		Label nextHour =
		new Label(
		"Mardi • 11:00 → 12:30"
		);

		nextHour
		.getStyleClass()
		.add("next-info");

		nextActivity
		.getChildren()
		.addAll(
		nextTitle,
		nextSport,
		nextHour
		);





		VBox content =
		new VBox(
		26,
		hero,
		nextActivity
		);

		content.setPadding(
		new Insets(
		30,
		40,
		40,
		40
		)
		);




		BorderPane root =
		new BorderPane();

		root.setTop(
		MemberNavbar.create(
		stage,
		"accueil",
		login
		)
		);

		root.setCenter(
		content
		);

		root.getStyleClass()
		.add(
		"page-background"
		);

		Scene scene =
		new Scene(
		root,
		1400,
		900
		);

		scene.getStylesheets()
		.add(
		getClass()
		.getResource(
		"/resources/style.css"
		)
		.toExternalForm()
		);

		stage.setScene(scene);

		stage.setMaximized(true);

		stage.show();

		}

	private VBox createCard(
			String icon,
			String title,
			String buttonText
			){

			Label i =
			new Label(icon);

			i.getStyleClass()
			.add("member-icon");

			Label t =
			new Label(title);

			t.getStyleClass()
			.add("member-card-title");

			Label b =
			new Label(buttonText);

			b.getStyleClass()
			.add("member-card-btn");

			VBox box =
			new VBox(
			20,
			i,
			t,
			b
			);

			box.setAlignment(
			Pos.CENTER
			);

			box.setPrefSize(
			320,
			220
			);

			box.getStyleClass()
			.add(
			"member-card"
			);

			return box;

			}
}