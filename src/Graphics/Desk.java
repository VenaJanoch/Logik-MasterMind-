package Graphics;

import Control.Constants;
import Control.Logics;
import Run.MasterMindRun;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Desk extends Stage {

	private MasterMindRun mMR;
	private Scene newScena;
	private BorderPane hlavniPanel;

	private KnobPanel[] knobPanel;
	private ControlKnobsPanel[] controlKnobPanel;
	private Logics logics;
	private KnobPanel result;
	private ColorPalet cp;
	private HBox lineBox[];

	protected Label statutL;
	protected Button retryB;
	protected Button closeB;
	
	private Button menuB;
	private Button singOutB;
	
	public Desk(MasterMindRun mMR) {

		super();
		this.mMR = mMR;
		this.logics = new Logics(this);
		this.cp = new ColorPalet(logics);
		this.knobPanel = new KnobPanel[Constants.countKnobsPanels];
		this.lineBox = new HBox[Constants.countKnobsPanels];
		this.controlKnobPanel = new ControlKnobsPanel[Constants.countKnobsPanels];
		
		this.setTitle("MasterMind-GameWindow");

		this.setScene(creatScene());

	}

	public Scene creatScene() {

		newScena = new Scene(creatPanel(), 310, 600);
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
		hlavniPanel.setTop(createMenuBar());
		
		hlavniPanel.setBackground(new Background(new BackgroundFill(Color.BURLYWOOD, CornerRadii.EMPTY, Insets.EMPTY)));
		hlavniPanel.setPadding(new Insets(8));
		return hlavniPanel;
	}



	private Node createMenuBar() {
		HBox menuBar = new HBox(5);
		
		menuB = new Button("Menu");
		singOutB = new Button("Sing out");
		
		menuB.setOnAction(event -> mMR.setWellcomeWindow());
	
		menuBar.getChildren().addAll(menuB,singOutB);
		
		menuBar.setAlignment(Pos.CENTER_LEFT);
		menuBar.setPadding(new Insets(3,3,3,5));
		return menuBar;
	}

	public Node creatGameDesk() {
		createKnobsPanels();

		VBox desk = new VBox(5);

		createLineBox();

		desk.getChildren().addAll(lineBox);

		return desk;
	}

	private void createLineBox() {

		for (int i = 0; i < lineBox.length; i++) {
			lineBox[i] = new HBox(5);

			lineBox[i].getChildren().add(controlKnobPanel[i]);
			lineBox[i].getChildren().add(knobPanel[i]);

		}
	}

	private void createKnobsPanels() {

		knobPanel[0] = new KnobPanel(0, this, cp);
		controlKnobPanel[0] = new ControlKnobsPanel(0, this);

		for (int i = 1; i < knobPanel.length; i++) {

			knobPanel[i] = new KnobPanel(i, this, cp);
			controlKnobPanel[i] = new ControlKnobsPanel(i, this);

			knobPanel[i].setVisible(false);
			controlKnobPanel[i].setVisible(false);
		}
	}

	/*********** Getrs and Setrs **************/
	public KnobPanel[] getKnobPanel() {
		return knobPanel;
	}

	public void setKnobPanel(KnobPanel[] knobPanel) {
		this.knobPanel = knobPanel;
	}

	public KnobPanel getResult() {
		return result;
	}

	public void setResult(KnobPanel result) {
		this.result = result;
	}

	public ControlKnobsPanel[] getControlKnobPanel() {
		return controlKnobPanel;
	}

	public void setControlKnobPanel(ControlKnobsPanel[] controlKnobPanel) {
		this.controlKnobPanel = controlKnobPanel;
	}

	public Logics getLogics() {
		return logics;
	}

	public void setLogics(Logics logics) {
		this.logics = logics;
	}

	public Label getStatutL() {
		return statutL;
	}

	public void setStatutL(Label statutL) {
		this.statutL = statutL;
	}

	public MasterMindRun getmMR() {
		return mMR;
	}

	public void setmMR(MasterMindRun mMR) {
		this.mMR = mMR;
	}

	public ColorPalet getCp() {
		return cp;
	}

	public void setCp(ColorPalet cp) {
		this.cp = cp;
	}

	public BorderPane getHlavniPanel() {
		return hlavniPanel;
	}

	public void setHlavniPanel(BorderPane hlavniPanel) {
		this.hlavniPanel = hlavniPanel;
	}

	
	

}
