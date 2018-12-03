package jaims_development_studio.jaims.clientfx.gui;

import jaims_development_studio.jaims.clientfx.logic.ClientFXMain;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GuiMain {

	private Stage primaryStage;

	public GuiMain(Stage primaryStage, ClientFXMain clientMain) {

		this.primaryStage = primaryStage;
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		LoginScene sp = new LoginScene();
		sp.setId("background-pane");
		Scene s = new Scene(sp, 500, 800);
		s.setFill(Color.TRANSPARENT);
		s.getStylesheets().add("cssFiles/login.css");
		primaryStage.setScene(s);
		primaryStage.show();

	}

	private void showLoginScene() {

	}

}
