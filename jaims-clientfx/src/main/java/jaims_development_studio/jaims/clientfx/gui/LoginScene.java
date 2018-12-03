package jaims_development_studio.jaims.clientfx.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginScene extends VBox {

	public LoginScene() {

		getStylesheets().add("cssFiles/login.css");
		setId("background-pane");

		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setSpacing(20);
		Label lSignIn = new Label("Sign In");
		lSignIn.setId("sign-in-pane");
		hbox.getChildren().add(lSignIn);

		Label lSignUp = new Label("Sign Up");
		lSignUp.setId("sign-in-pane");
		hbox.getChildren().add(lSignUp);

		getChildren().add(hbox);

	}

}
