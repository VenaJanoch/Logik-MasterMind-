package Graphics;

import Control.LogginLogics;
import Control.NetworkLogics;
import Control.ObservableText;
import Control.ObservingLabel;
import Interfaces.IGameMode;
import Run.MasterMindRun;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MultiMode extends Desk implements IGameMode {

	private ObservingLabel statutL;
	private Label conectLB;
	private ObservableText obserText = new ObservableText("Find color combination");

	public MultiMode(MasterMindRun mMR, NetworkLogics netLog, LogginLogics lLog) {
		super(mMR, netLog, lLog,true);
		setNetLog(netLog);
		getHlavniPanel().setBottom(creatResultPanel());
		getHlavniPanel().setLeft(creatLegendPanel());
	}

	@Override
	public HBox creatResultPanel() {
		HBox resultPanel = new HBox(5);
		getLogics().setMultiMode(true);
		Label resultLB = new Label("Color result: ");

		resultLB.setFont(Font.font("Verdana", FontWeight.BOLD, 13));

		
		setResult(new KnobPanel(100, this, getCp(),getLogics(),getNetLog()));
		
		resultPanel.getChildren().add(resultLB);
		resultPanel.getChildren().add(getResult());

		System.out.println("Hrac " + getNetLog().getName() + " je vyzivatel " + getNetLog().isChallenger() );
		
		if (getNetLog().isChallenger()) {
			
		getResult().setVisible(false);
			
		}else{
			
			obserText.inc("Create color combination");
		}
		return resultPanel;
	}
	
	@Override
	public void resetDesk(){

		getNetLog().leaveGame();
		getmMR().setWellcomeWindow();
	}
	/**
	 * Pomocna metoda pro ukonceni hry
	 */
	public void exitGame(){
		getNetLog().signOutUser("LogOut,end,\n");
		getmMR().getPrimaryStage().close();
	}

	public void freezDesk(){
		conectLB.setVisible(true);
		leaveB.setDisable(true);
		closeB.setDisable(true);
		getMenuB().setDisable(true);
		getSingOutB().setDisable(true);
		
	}
	
public void unFreezDesk(){
		conectLB.setVisible(false);
		leaveB.setDisable(false);
		closeB.setDisable(false);
		getMenuB().setDisable(false);
		getSingOutB().setDisable(false);
		
	}
	
	@Override
	public Node creatLegendPanel() {

		VBox legendPanel = new VBox(4);
		HBox buttonPanel = new HBox(4);

		statutL = new ObservingLabel();
		obserText.addObserver(statutL);
		leaveB = new Button("Leave");
		closeB = new Button("Exit");
		conectLB = new Label("Waiting for server");
		conectLB.setVisible(false);
		statutL.setFont(Font.font("Verdana", FontWeight.BOLD, 13));

		leaveB.setOnAction(event -> resetDesk());
		closeB.setOnAction(event -> exitGame());

		buttonPanel.getChildren().addAll(leaveB, closeB);
		buttonPanel.setAlignment(Pos.CENTER);
		legendPanel.getChildren().addAll(conectLB,getCp(), statutL, buttonPanel);

		legendPanel.setAlignment(Pos.CENTER);

		legendPanel.setMaxWidth(170);
		legendPanel.setMinWidth(170);

		return legendPanel;

	}
	
	/** Getrs and Setrs **/
	
	public ObservableText getObserText() {
		return obserText;
	}

	public void setObserText(ObservableText obserText) {
		this.obserText = obserText;
	}

	public Label getConectLB() {
		return conectLB;
	}

	public void setConectLB(Label conectLB) {
		this.conectLB = conectLB;
	}

}
