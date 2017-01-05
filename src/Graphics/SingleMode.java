package Graphics;

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

public class SingleMode extends Desk implements IGameMode {

	/**
	 * Inicializace objektu mMR
	 * @param mMR
	 */
	public SingleMode(MasterMindRun mMR) {
		super(mMR);
		getHlavniPanel().setBottom(creatResultPanel());
		getHlavniPanel().setLeft(creatLegendPanel());

	}

	@Override
	public HBox creatResultPanel() {
		HBox resultPanel = new HBox(5);
		getLogics().setMultiMode(false);
		Label resultLB = new Label("Color result: ");

		resultLB.setFont(Font.font("Verdana", FontWeight.BOLD, 13));

		setResult(new KnobPanel(100, this, super.getCp(),getLogics()));
		super.getResult().setVisible(false);
		super.getResult().setResultColor(super.getLogics().creatResultColors());

		resultPanel.getChildren().add(resultLB);
		resultPanel.getChildren().add(super.getResult());

		return resultPanel;
	}

	public Node creatLegendPanel() {

		VBox legendPanel = new VBox(4);
		HBox buttonPanel = new HBox(4);

		statuL = new Label("Find color combination");
		retryB = new Button("Retry");
		closeB = new Button("Exit");

		statuL.setFont(Font.font("Verdana", FontWeight.BOLD, 13));

		retryB.setOnAction(event -> resetDesk());
		closeB.setOnAction(event -> getmMR().getPrimaryStage().close());

		buttonPanel.getChildren().addAll(retryB, closeB);
		buttonPanel.setAlignment(Pos.CENTER);
		legendPanel.getChildren().addAll(getCp(), statuL, buttonPanel);

		legendPanel.setAlignment(Pos.CENTER);

		legendPanel.setMaxWidth(170);
		legendPanel.setMinWidth(170);

		return legendPanel;

	}

	@Override
	public void resetDesk() {
		getHlavniPanel().setCenter(creatGameDesk());
		getHlavniPanel().setLeft(creatLegendPanel());
		getHlavniPanel().setBottom(creatResultPanel());

	}

}
