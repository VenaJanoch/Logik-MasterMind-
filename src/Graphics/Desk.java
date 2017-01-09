package Graphics;

import Control.Constants;
import Control.LogginLogics;
import Control.Logics;
import Control.NetworkLogics;
import Control.ObservableText;
import Control.ObservingLabel;
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

	/** Globalni promenne tridy**/
	private MasterMindRun mMR;
	private Scene newScena;
	private BorderPane hlavniPanel;
	
	

	private KnobPanel[] knobPanel;
	private ControlKnobsPanel[] controlKnobPanel;
	private Logics logics;
	private LogginLogics lLog;
	private NetworkLogics netLog;
	private KnobPanel result;
	private ColorPalet cp;
	private HBox lineBox[];

	protected Label statuL;
	protected Button retryB;
	protected Button leaveB;
	protected Button closeB;
	
	private Button menuB;
	private Button singOutB;
	
	/**
	 * Inicializace objektu mMR, netLog, lLog, multiMode
	 * @param mMR
	 * @param netLog
	 * @param lLog
	 * @param multiMode
	 */
	
	public Desk(MasterMindRun mMR, NetworkLogics netLog, LogginLogics lLog, boolean multiMode) {

		super();
		this.mMR = mMR;
		this.netLog = netLog;
		this.logics = new Logics(this, multiMode);
		this.cp = new ColorPalet(logics,netLog);
		this.knobPanel = new KnobPanel[Constants.countKnobsPanels];
		this.lineBox = new HBox[Constants.countKnobsPanels];
		this.controlKnobPanel = new ControlKnobsPanel[Constants.countKnobsPanels];
		this.lLog = lLog;
		this.setTitle("MasterMind-GameWindow");

		this.setScene(creatScene());

	}

	/**
	 * Pretizeny konstruktor
	 * 
	 * @param mMR
	 */
	public Desk(MasterMindRun mMR) {

		super();
		this.mMR = mMR;
		this.logics = new Logics(this,false);
		this.cp = new ColorPalet(logics,mMR.getNetLog());
		this.knobPanel = new KnobPanel[Constants.countKnobsPanels];
		this.lineBox = new HBox[Constants.countKnobsPanels];
		this.controlKnobPanel = new ControlKnobsPanel[Constants.countKnobsPanels];
		this.lLog = mMR.getLogLogics();
		this.setTitle("MasterMind-GameWindow");

		this.setScene(creatScene());

	}
	public Scene creatScene() {

		newScena = new Scene(creatPanel(), Constants.width, Constants.height);
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


	/**
	 * Metoda pro vytvoreni menu panelu
	 * 
	 * @return BorderPane
	 */
	private Node createMenuBar() {
		HBox menuBar = new HBox(5);
		
		menuB = new Button("Menu");
		singOutB = new Button("Sing out");
		
		menuB.setOnAction(event -> mMR.setWellcomeWindow());
	
		singOutB.setOnAction(event -> netLog.signOutUser("LogOut,Desk\n"));
		menuBar.getChildren().addAll(menuB,singOutB);
		
		menuBar.setAlignment(Pos.CENTER_LEFT);
		menuBar.setPadding(new Insets(3,3,3,5));
		return menuBar;
	}

	/**
	 * Vytvoreni hraci plochu 
	 * 
	 * @return BorderPane
	 */
	public Node creatGameDesk() {
		createKnobsPanels();

		VBox desk = new VBox(5);

		createLineBox();

		desk.getChildren().addAll(lineBox);

		return desk;
	}
	
	/**
	 * Predela hraci stul
	 */
	public void repaintDesk(){
		
		VBox desk = new VBox(5);

		
		knobPanel[0].setBackground(new Background(new BackgroundFill(Constants.colors[Integer.parseInt("1")], CornerRadii.EMPTY, Insets.EMPTY)));	
		
		desk.getChildren().addAll(lineBox);
		hlavniPanel.setCenter(desk);
	}
	
	/**
	 *  createLineBox()
	 *  Vytvori jednotlive hraci tahy
	 **/
	private void createLineBox() {

		for (int i = 0; i < lineBox.length; i++) {
			lineBox[i] = new HBox(5);

			lineBox[i].getChildren().add(controlKnobPanel[i]);
			lineBox[i].getChildren().add(knobPanel[i]);

		}
	}

	/**
	 * createKnobsPanels()
	 * Vytvori hraci tlacitka
	 */
	private void createKnobsPanels() {

		knobPanel[0] = new KnobPanel(0, this, cp, logics, netLog);
		controlKnobPanel[0] = new ControlKnobsPanel(0, this);

		
			knobPanel[0].nothig();
		
		
		for (int i = 1; i < knobPanel.length; i++) {

			knobPanel[i] = new KnobPanel(i, this, cp,logics, netLog);
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

	public LogginLogics getlLog() {
		return lLog;
	}

	public void setlLog(LogginLogics lLog) {
		this.lLog = lLog;
	}

	public NetworkLogics getNetLog() {
		return netLog;
	}

	public void setNetLog(NetworkLogics netLog) {
		this.netLog = netLog;
	}

	public Label getStatutL() {
		return statuL;
	}

	public void setStatutL(Label statuL) {
		this.statuL = statuL;
	}

	
	

}
