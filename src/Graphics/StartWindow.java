package Graphics;

import Control.Constants;
import Control.Logics;
import Run.MasterMindRun;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class StartWindow extends Stage {

	private MasterMindRun mMR;
	private Scene newScena;
	private BorderPane hlavniPanel;

	private KnobPanel[] knobPanel;
	private ControlKnobsPanel[] controlKnobPanel;
	private Logics logics;

	public StartWindow(MasterMindRun mMR) {

		super();
		this.mMR = mMR;

		knobPanel = new KnobPanel[Constants.countKnobsPanels];
		controlKnobPanel = new ControlKnobsPanel[Constants.countKnobsPanels];
		logics = new Logics();
		this.setTitle("MasterMind-Menu");

		this.setScene(creatScene());

	}

	public Scene creatScene() {

		newScena = new Scene(creatPanel(), 300, 500);
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
		hlavniPanel.setPadding(new Insets(8));
		return hlavniPanel;
	}

	private Node creatColorPanel() {

		return null;
	}

	private Node creatLegendPanel() {

		// TODO Auto-generated method stub
		return null;

	}

	private Node creatGameDesk() {
		createKnobsPanels();
		
		BorderPane desk = new BorderPane();
		
		VBox leftDesk = new VBox(5);
		VBox rightDesk = new VBox(5);
		
		rightDesk.getChildren().addAll(knobPanel);
		leftDesk.getChildren().addAll(controlKnobPanel);
		
		desk.setLeft(leftDesk);
		desk.setCenter(rightDesk);
		
		desk.setPadding(new Insets(5));
		return desk;
	}

	private void createKnobsPanels() {

		for (int i = 0; i < knobPanel.length; i++) {

			knobPanel[i] = new KnobPanel(i, this);
			controlKnobPanel[i] = new ControlKnobsPanel(i,this);

			if (i != 0) {
				knobPanel[i].setVisible(false);
				controlKnobPanel[i].setVisible(false);
			}
		}
	}

	/*********** Getrs and Setrs **************/
	public KnobPanel[] getKnobPanel() {
		return knobPanel;
	}

	public void setKnobPanel(KnobPanel[] knobPanel) {
		this.knobPanel = knobPanel;
	}

}
