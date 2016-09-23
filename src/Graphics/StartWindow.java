package Graphics;

import com.sun.javafx.scene.control.skin.ColorPalette;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class StartWindow extends Stage {

	private MasterMindRun mMR;
	private Scene newScena;
	private BorderPane hlavniPanel;

	private KnobPanel[] knobPanel;
	private ControlKnobsPanel[] controlKnobPanel;
	private Logics logics;
	private KnobPanel result;
	private ColorPalet cp;
	private HBox lineBox[];

	private Label statutL;
	private Button retryB;
	private Button closeB;

	public StartWindow(MasterMindRun mMR) {

		super();
		this.mMR = mMR;
		cp = new ColorPalet();
		knobPanel = new KnobPanel[Constants.countKnobsPanels];
		lineBox = new HBox[Constants.countKnobsPanels];
		controlKnobPanel = new ControlKnobsPanel[Constants.countKnobsPanels];
		logics = new Logics();
		this.setTitle("MasterMind-Menu");

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
		hlavniPanel.setLeft(creatLegendPanel());
		hlavniPanel.setBottom(creatResultPanel());

		hlavniPanel.setBackground(new Background(new BackgroundFill(Color.BURLYWOOD, CornerRadii.EMPTY, Insets.EMPTY)));
		hlavniPanel.setPadding(new Insets(8));
		return hlavniPanel;
	}

	private Node creatResultPanel() {

		HBox resultPanel = new HBox(5);
		Label resultLB = new Label("Color result: ");

		resultLB.setFont(Font.font("Verdana", FontWeight.BOLD, 13));

		setResult(new KnobPanel(100, this, cp));
		result.setVisible(false);

		result.setResultColor(logics.creatResultColors());

		resultPanel.getChildren().add(resultLB);
		resultPanel.getChildren().add(result);

		return resultPanel;
	}

	private Node creatLegendPanel() {

		VBox legendPanel = new VBox(4);
		HBox buttonPanel = new HBox(4);

		statutL = new Label("Find color combination");
		retryB = new Button("Retry");
		closeB = new Button("Exit");

		statutL.setFont(Font.font("Verdana", FontWeight.BOLD, 13));

		retryB.setOnAction(event -> resetDesk());
		closeB.setOnAction(event -> mMR.getPrimaryStage().close());

		buttonPanel.getChildren().addAll(retryB, closeB);
		buttonPanel.setAlignment(Pos.CENTER);
		legendPanel.getChildren().addAll(cp, statutL, buttonPanel);

		legendPanel.setAlignment(Pos.CENTER);

		legendPanel.setMaxWidth(170);
		legendPanel.setMinWidth(170);

		return legendPanel;

	}

	private void resetDesk() {

		hlavniPanel.setCenter(creatGameDesk());
		hlavniPanel.setLeft(creatLegendPanel());
		hlavniPanel.setBottom(creatResultPanel());

	}

	private Node creatGameDesk() {
		createKnobsPanels();

		
		VBox desk = new VBox(5);
		
		createLineBox();
		
		desk.getChildren().addAll(lineBox);

		return desk;
	}

	private void createLineBox(){
		
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

}
