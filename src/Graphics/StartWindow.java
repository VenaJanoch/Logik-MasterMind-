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
	
	private Label statutL;
	private Button retryB;
	private Button closeB;
	
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
		
		resultLB.setFont(Font.font("Verdana", FontWeight.BOLD,13));
		
		setResult(new KnobPanel(100, this));
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
		
		
		statutL.setFont(Font.font("Verdana", FontWeight.BOLD,13));
		
		retryB.setOnAction(event -> resetDesk());
		closeB.setOnAction(event -> mMR.getPrimaryStage().close());
		
		buttonPanel.getChildren().addAll(retryB,closeB);
		buttonPanel.setAlignment(Pos.CENTER);
		legendPanel.getChildren().addAll(statutL, buttonPanel);
		
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
		
		HBox desk = new HBox(4);
		
		VBox leftDesk = new VBox(5);
		VBox rightDesk = new VBox(5);
		
		rightDesk.getChildren().addAll(knobPanel);
		leftDesk.getChildren().addAll(controlKnobPanel);
		
		
		desk.getChildren().add(leftDesk);
		desk.getChildren().add(rightDesk);
		
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
