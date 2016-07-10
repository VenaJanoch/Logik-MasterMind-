package Run;

import Graphics.StartWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class MasterMindRun extends Application{
	
	
	/**
	 * Metoda main
	 * Zavola launch a spusti program 
	 * @param args
	 */
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/** Atributy tridy **/
	private Stage primaryStage;
	private StartWindow stWindow;
	
	
	
	/**
	 * Metoda start
	 * Prepsana trida pro vytvoreni okna
	 */
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		stWindow = new StartWindow(this);
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(stWindow.getTitle());
		this.primaryStage.setScene(stWindow.getScene());

		this.primaryStage.show();
	}



}
