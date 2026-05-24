package view;

import controller.StatController;

import javafx.geometry.*;

import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.scene.layout.*;

import javafx.stage.Stage;

import model.StatMembre;

import java.util.List;

public class StatView {

private StatController
controller=
new StatController();

private VBox results=
new VBox(
20
);

public void show(
Stage stage
){

Label title=
new Label(
"Classement des membres"
);

title
.getStyleClass()
.add(
"page-title"
);

DatePicker debut=
new DatePicker();

DatePicker fin=
new DatePicker();

Button btn=
new Button(
"Afficher"
);

btn
.getStyleClass()
.add(
"login-button"
);

btn.setOnAction(
e->{

if(
debut.getValue()==null
||
fin.getValue()==null
){

return;

}

afficherStats(

controller
.getStats(

debut
.getValue()
.toString(),

fin
.getValue()
.toString()

)

);

}
);

HBox filters=
new HBox(
15,
debut,
fin,
btn
);

ScrollPane scroll=
new ScrollPane(
results
);

scroll.setFitToWidth(
true
);

VBox content=
new VBox(
25,
title,
filters,
scroll
);

content.setPadding(
new Insets(
30
)
);

BorderPane root=
new BorderPane();

root.setTop(
Navbar.create(
stage,
"stat"
)
);

root.setCenter(
content
);

Scene scene=
new Scene(
root,
1400,
800
);

scene
.getStylesheets()
.add(

getClass()

.getResource(
"/resources/style.css"
)

.toExternalForm()

);

stage.setScene(
scene);

stage.show();

}
private void afficherStats(List<StatMembre> stats) {

    results.getChildren().clear();

    if (stats.isEmpty()) {
        Label empty = new Label("Aucune donnée pour cette période");
        empty.getStyleClass().add("member-empty-text");
        results.getChildren().add(empty);
        return;
    }

    HBox podium = new HBox(24);
    podium.setAlignment(Pos.CENTER);
    podium.getStyleClass().add("podium-section");

    for (int i = 0; i < Math.min(3, stats.size()); i++) {

        StatMembre m = stats.get(i);

        Label rank = new Label(i == 0 ? "🏆 #1" : i == 1 ? "🥈 #2" : "🥉 #3");
        rank.getStyleClass().add("podium-rank");

        Label name = new Label(m.getNomComplet());
        name.getStyleClass().add("podium-name");

        Label meta = new Label(m.getAge() + " ans");
        meta.getStyleClass().add("podium-meta");

        Label score = new Label("🔥 " + m.getTotalActivites() + " activités");
        score.getStyleClass().add("podium-score");

        VBox card = new VBox(14, rank, name, meta, score);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add(i == 0 ? "podium-card-first" : "podium-card-modern");

        podium.getChildren().add(card);
    }

    results.getChildren().add(podium);

    for (int i = 3; i < stats.size(); i++) {

        StatMembre m = stats.get(i);

        Label rank = new Label("#" + (i + 1));
        rank.getStyleClass().add("rank-number");

        Label name = new Label(m.getNomComplet());
        name.getStyleClass().add("rank-name");

        Label age = new Label(m.getAge() + " ans");
        age.getStyleClass().add("rank-age");

        Label total = new Label("🔥 " + m.getTotalActivites());
        total.getStyleClass().add("rank-score");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(20, rank, name, age, spacer, total);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("ranking-row-modern");

        results.getChildren().add(row);
    }
}

}