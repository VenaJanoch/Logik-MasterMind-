package Graphics;

import Run.MasterMindRun;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class StartWindow extends Stage{

	MasterMindRun mMR;
	Scene newScena;
	BorderPane hlavniPanel;
	
	public StartWindow(MasterMindRun mMR) {
		super();
		this.mMR = mMR;
		
		this.setTitle("MasterMind-Menu");

		this.setScene(creatScene());
	
	
	}


	public Scene creatScene() {

		newScena = new Scene(creatPanel(), 750, 300);
		return newScena;

	}

	/**
	 * Metoda pro vytvoreni korenoveho panelu okna
	 * 
	 * @return BorderPane
	 */
	public Parent creatPanel() {
		hlavniPanel = new BorderPane();
		hlavniPanel.setCenter(creatGameDesk());
		hlavniPanel.setLeft(creatLegendPanel());
		hlavniPanel.setBottom(creatColorPanel());
		
		
		hlavniPanel.setBackground(new Background(new BackgroundFill(Color.HONEYDEW, CornerRadii.EMPTY, Insets.EMPTY)));

		return hlavniPanel;
	}


	private Node creatColorPanel() {
		
		return null;
	}


	private Node creatLegendPanel() {
		
		KnobPanel kp = new KnobPanel(0);
		
		return kp;
	}


	private Node creatGameDesk() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
