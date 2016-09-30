package Run;

import Graphics.MultiMode;
import Graphics.SingleMode;
import Graphics.StartWindow;
import Graphics.WellcomeWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import sun.print.resources.serviceui;

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
	private WellcomeWindow wellcome;
	private SingleMode sM;
	private MultiMode mM;
	
	
	
	/**
	 * Metoda start
	 * Prepsana trida pro vytvoreni okna
	 */
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		wellcome = new WellcomeWindow(this);
		this.primaryStage = primaryStage;
		
		this.setStage(wellcome);
		
		this.primaryStage.show();
	}


	public void setStage(Stage stage){
		
		this.primaryStage.setTitle(stage.getTitle());
		this.primaryStage.setScene(stage.getScene());

	}

	public void setGameWindowSingleMode(){
		//stWindow = new StartWindow(this,true);
		sM= new SingleMode(this);
		
		setStage(sM);
	}
	
	public void setGameWindowMultiMode(){
		mM = new MultiMode(this);
		setStage(mM);
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}



	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}



}
