package view;

import dao.UserDAO;

import javafx.geometry.*;

import javafx.scene.*;

import javafx.scene.control.*;

import javafx.scene.layout.*;

import javafx.stage.Stage;

public class VerifyCodeView {

public void show(
Stage stage,
String login
){

VBox root =
new VBox(20);

root.setAlignment(
Pos.CENTER
);

TextField code =
new TextField();

code.setPromptText(
"Code"
);

Button btn =
new Button(
"Vérifier"
);

Label msg =
new Label();

btn.setOnAction(e->{

boolean ok =
new UserDAO()
.verifierCode(
login,
code.getText()
);

if(ok){

new ChangePasswordView()
.show(
stage,
login
);

}

else{

msg.setText(
"Code incorrect"
);

}

});

root.getChildren()
.addAll(
code,
btn,
msg
);

stage.setScene(
new Scene(
root,
450,
350
)
);

}

}