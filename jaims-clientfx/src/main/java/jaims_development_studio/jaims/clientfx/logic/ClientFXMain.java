package jaims_development_studio.jaims.clientfx.logic;

import jaims_development_studio.jaims.clientfx.gui.GuiMain;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientFXMain extends Application {

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		System.out.println("Main method");
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// TODO Auto-generated method stub
		System.out.println("start method");
		new GuiMain(primaryStage, this);
	}

}
